package repository

import zio._

case class Task(id: Int, description: String, isDone: Boolean)

trait TaskRepository {
    def add(task: Task): UIO[Task]
    def update(id: Int, description: Option[String], isDone: Option[Boolean]): UIO[Option[Task]]
    def list(): UIO[List[Task]]
    def delete(id: Int): UIO[Boolean]
}

object TaskRepository {
    val live: ULayer[TaskRepository] = ZLayer {
        for {
            ref <- Ref.make(List.empty[Task]) // Armazena as tarefas em uma lista
        } yield new TaskRepository {
            override def add(task: Task): UIO[Task] =
                ref.updateAndGet(tasks => task :: tasks).as(task)
            
            override def update(id: Int, description: Option[String], isDone: Option[Boolean]): UIO[Option[Task]] =
                ref.modify { tasks =>
                    val updatedTasks = tasks.map {
                        case t if t.id == id =>
                            t.copy(
                                description = description.getOrElse(t.description),
                                isDone = isDone.getOrElse(t.isDone)
                            )
                        case t => t
                    }
                    (updatedTasks.find(_.id == id), updatedTasks)
                }
            
            override def list(): UIO[List[Task]] =
                ref.get
            
            override def delete(id: Int): UIO[Boolean] =
                ref.modify { tasks =>
                    val (remaining, removed) = tasks.partition(_.id != id)
                    (removed.nonEmpty, remaining)
                }
        }
    }
}

