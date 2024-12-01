package tasks

import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{Escape, H2ZioJdbcContext}
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.*
import tasks.Task

import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

case class TaskTable(uuid: UUID, title: String, description: String, isCompleted: Boolean)

case class PersistentTaskRepo(ds: DataSource) extends TaskRepo:
    val ctx = new H2ZioJdbcContext(Escape)

    import ctx._

    override def register(task: Task): zio.Task[String] = {
        for
            id <- Random.nextUUID
            _ <- ctx.run {
                quote {
                    query[TaskTable].insertValue {
                        lift(TaskTable(id, task.title, task.description, task.isCompleted))
                    }
                }
            }
        yield id.toString
    }.provide(ZLayer.succeed(ds))

    // Função de update
    override def update(id: String, updatedTask: Task): zio.Task[Boolean] = {
        val uuid = UUID.fromString(id)
        val updatedAt = Some(java.time.Instant.now()) // Marca a hora de atualização

        ctx
          .run {
              quote {
                  query[TaskTable]
                    .filter(p => p.uuid == lift(uuid))
                    .update(
                        _.title -> lift(updatedTask.title),
                        _.description -> lift(updatedTask.description),
                        _.isCompleted -> lift(updatedTask.isCompleted),
                    )
              }
          }
          .provide(ZLayer.succeed(ds))
          .map(_ > 0) // Retorna true se alguma linha foi atualizada, false caso contrário
    }

    override def lookup(id: String): zio.Task[Option[Task]] =
        ctx
          .run {
              quote {
                  query[TaskTable]
                    .filter(p => p.uuid == lift(UUID.fromString(id)))
                    .map(t => Task(t.title, t.description, t.isCompleted))
              }
          }
          .provide(ZLayer.succeed(ds))
          .map(_.headOption)

    override def tasks: zio.Task[List[Task]] =
        ctx
          .run {
              quote {
                  query[TaskTable].map(t => Task(t.title, t.description, t.isCompleted))
              }
          }
          .provide(ZLayer.succeed(ds))

object PersistentTaskRepo:
    def layer: ZLayer[Any, Throwable, PersistentTaskRepo] =
        Quill.DataSource.fromPrefix("TaskApp") >>>
          ZLayer.fromFunction(PersistentTaskRepo(_))