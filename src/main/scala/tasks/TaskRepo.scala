package tasks

import tasks.Task
import zio.*

trait TaskRepo:
    def register(user: Task): zio.Task[String]

    def lookup(id: String): zio.Task[Option[Task]]

    def tasks: zio.Task[List[Task]]

object TaskRepo:
    def register(user: Task): ZIO[TaskRepo, Throwable, String] =
        ZIO.serviceWithZIO[TaskRepo](_.register(user))

    def lookup(id: String): ZIO[TaskRepo, Throwable, Option[Task]] =
        ZIO.serviceWithZIO[TaskRepo](_.lookup(id))

    def tasks: ZIO[TaskRepo, Throwable, List[Task]] =
        ZIO.serviceWithZIO[TaskRepo](_.tasks)