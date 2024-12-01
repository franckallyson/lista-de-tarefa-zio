package tasks

import tasks.Task
import zio.*

trait TaskRepo:
    def register(task: Task): zio.Task[String]
    
    def update(id: String, updatedTask: Task): zio.Task[Boolean]
    
    def lookup(id: String): zio.Task[Option[Task]]

    def tasks: zio.Task[List[Task]]


object TaskRepo:
    def register(task: Task): ZIO[TaskRepo, Throwable, String] =
        ZIO.serviceWithZIO[TaskRepo](_.register(task))

    def update(id: String, updatedTask: Task): ZIO[TaskRepo, Throwable, Boolean] =
        ZIO.serviceWithZIO[TaskRepo](_.update(id, updatedTask))
        
    def lookup(id: String): ZIO[TaskRepo, Throwable, Option[Task]] =
        ZIO.serviceWithZIO[TaskRepo](_.lookup(id))

    def tasks: ZIO[TaskRepo, Throwable, List[Task]] =
        ZIO.serviceWithZIO[TaskRepo](_.tasks)