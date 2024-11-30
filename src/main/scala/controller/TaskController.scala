package controller

import service.*
import repository.*
import zio.*
import zio.http.*
import zio.http.model.{Method, Status}
import zio.json.*

object TaskController {
    def routes: HttpApp[TaskService, Throwable] =
        Http.collectZIO[Request] {
            case req @ Method.POST -> !! / "tasks" =>
                for {
                    body <- req.body.asString
                    description <- ZIO.fromEither(body.fromJson[String]).orElseFail(new Exception("Invalid JSON"))
                    task <- ZIO.serviceWithZIO[TaskService](_.addTask(description))
                } yield Response.json(task.toJson)
            
            case Method.GET -> !! / "tasks" =>
                for {
                    tasks <- ZIO.serviceWithZIO[TaskService](_.listTasks())
                } yield Response.json(tasks.toJson)
            
            case req @ Method.PUT -> !! / "tasks" / int(id) =>
                for {
                    body <- req.body.asString
                    data <- ZIO.fromEither(body.fromJson[Map[String, Option[Any]]]).orElseFail(new Exception("Invalid JSON"))
                    description = data.get("description").flatMap(_.collect { case s: String => s })
                    isDone = data.get("isDone").flatMap(_.collect { case b: Boolean => b })
                    updatedTask <- ZIO.serviceWithZIO[TaskService](_.updateTask(id, description, isDone))
                } yield updatedTask.fold(Response.status(Status.NotFound))(task => Response.json(task.toJson))
            
            case Method.DELETE -> !! / "tasks" / int(id) =>
                for {
                    deleted <- ZIO.serviceWithZIO[TaskService](_.deleteTask(id))
                } yield if (deleted) Response.status(Status.Ok) else Response.status(Status.NotFound)
        }
}
