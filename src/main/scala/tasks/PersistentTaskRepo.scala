package tasks

import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{Escape, H2ZioJdbcContext}
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.*
import tasks.Task

import java.util.UUID
import javax.sql.DataSource

case class TaskTable(uuid: UUID, name: String, description: String, completed: Boolean)

case class PersistentTaskRepo(ds: DataSource) extends TaskRepo:
    val ctx = new H2ZioJdbcContext(Escape)

    import ctx._

    override def register(task: Task): zio.Task[String] = {
        for
            id <- Random.nextUUID
            _ <- ctx.run {
                quote {
                    query[TaskTable].insertValue {
                        lift(TaskTable(id, task.name, task.description, task.completed))
                    }
                }
            }
        yield id.toString
    }.provide(ZLayer.succeed(ds))
    
    
    override def lookup(id: String): zio.Task[Option[Task]] =
        ctx
          .run {
              quote {
                  query[TaskTable]
                    .filter(p => p.uuid == lift(UUID.fromString(id)))
                    .map(u => Task(u.name, u.description, u.completed))
              }
          }
          .provide(ZLayer.succeed(ds))
          .map(_.headOption)

    override def tasks: zio.Task[List[Task]] =
        ctx
          .run {
              quote {
                  query[TaskTable].map(u => Task(u.name, u.description, u.completed))
              }
          }
          .provide(ZLayer.succeed(ds))

object PersistentTaskRepo:
    def layer: ZLayer[Any, Throwable, PersistentTaskRepo] =
        Quill.DataSource.fromPrefix("TaskApp") >>>
          ZLayer.fromFunction(PersistentTaskRepo(_))