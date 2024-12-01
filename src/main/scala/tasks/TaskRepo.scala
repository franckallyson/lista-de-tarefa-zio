package tasks

import tasks.{
    Task, TaskReturn
}
import zio.*

trait TaskRepo:
    def register(task: Task): zio.Task[String]
    
    def update(id: String, updatedTask: Task): zio.Task[Boolean]
    
    def lookup(id: String): zio.Task[Option[TaskReturn]]

    def tasks: zio.Task[List[TaskReturn]]

    def delete(id: String): zio.Task[Boolean]

object TaskRepo:
    def register(task: Task): ZIO[TaskRepo, Throwable, String] =
        ZIO.serviceWithZIO[TaskRepo](_.register(task))

    def update(id: String, updatedTask: Task): ZIO[TaskRepo, Throwable, Boolean] =
        ZIO.serviceWithZIO[TaskRepo](_.update(id, updatedTask))
        
    def lookup(id: String): ZIO[TaskRepo, Throwable, Option[TaskReturn]] =
        ZIO.serviceWithZIO[TaskRepo](_.lookup(id))

    def tasks: ZIO[TaskRepo, Throwable, List[TaskReturn]] =
        ZIO.serviceWithZIO[TaskRepo](_.tasks)

    def delete(id: String): ZIO[TaskRepo, Throwable, Boolean] = // Função DELETE
        ZIO.serviceWithZIO[TaskRepo](_.delete(id))