package service

import repository.{Task, TaskRepository}
import zio.*

trait TaskService {
    def addTask(description: String): UIO[Task]
    def updateTask(id: Int, description: Option[String], isDone: Option[Boolean]): UIO[Option[Task]]
    def listTasks(): UIO[List[Task]]
    def deleteTask(id: Int): UIO[Boolean]
}

object TaskService {
    val live: URLayer[TaskRepository, TaskService] = ZLayer {
        for {
            repo <- ZIO.service[TaskRepository]
        } yield new TaskService {
            override def addTask(description: String): UIO[Task] = {
                for {
                    tasks <- repo.list()
                    id = if (tasks.isEmpty) 1 else tasks.map(_.id).max + 1
                    task = Task(id, description, isDone = false)
                    _ <- repo.add(task)
                } yield task
            }
            
            override def updateTask(id: Int, description: Option[String], isDone: Option[Boolean]): UIO[Option[Task]] =
                repo.update(id, description, isDone)
            
            override def listTasks(): UIO[List[Task]] = repo.list()
            
            override def deleteTask(id: Int): UIO[Boolean] = repo.delete(id)
        }
    }
}
