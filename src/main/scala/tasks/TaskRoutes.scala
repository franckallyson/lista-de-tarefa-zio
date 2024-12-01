package tasks


import zio.*
import zio.http.*
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import tasks.{
    Task, TaskReturn
}
import zio.http.Header.AccessControlAllowOrigin.Specific
/** Collection of routes that:
 *   - Accept a `Request` and returns a `Response`
 *   - May fail with type of `Response`
 *   - Require a `UserRepo` from the environment
 */
object TaskRoutes:

    def apply(): Routes[TaskRepo, Response] =
        Routes(
            // POST /tasks -d '{"name": "John", "age": 35}'
            Method.POST / "tasks" -> handler { (req: Request) =>
                for {
                    u <- req.body.to[Task].orElseFail(Response.badRequest)
                    r <-
                      TaskRepo
                        .register(u)
                        .mapBoth(
                            _ =>
                                Response
                                  .internalServerError(s"Failed to register the task: $u"),
                            id => Response.text(id)
                        )
                } yield r
            },

            // GET /tasks/:id
            Method.GET / "tasks" / string("id") -> handler {
                (id: String, _: Request) =>
                    TaskRepo
                      .lookup(id)
                      .mapBoth(
                          _ => Response.internalServerError(s"Cannot retrieve task $id"),
                          {
                              case Some(taskReturn) =>
                                  Response(body = Body.from(taskReturn))
                              case None =>
                                  Response.notFound(s"Task $id not found!")
                          }
                      )
            },
            // GET /tasks
            Method.GET / "tasks" -> handler {
                TaskRepo.tasks.mapBoth(
                    _ => Response.internalServerError("Cannot retrieve tasks!"),
                    tasks => Response(body = Body.from(tasks))
                )
            },
            // PUT /tasks/:id -d '{"title": "...", "description": "...", "isCompleted": ...}'
            Method.PUT / "tasks" / string("id") -> handler { (id: String, req: Request) =>
                for {
                    updatedTask <- req.body.to[Task].orElseFail(Response.badRequest("Parametros enviados não correspondem"))
                    result <- TaskRepo
                      .update(id, updatedTask)
                      .mapBoth(
                          _ =>
                              Response
                                .internalServerError(s"Failed to update the task: $id"),
                          updated =>
                              if updated then
                                  Response.text(s"Task $id updated successfully!")
                              else
                                  Response.notFound(s"Task $id not found!")
                      )
                } yield result
            },
            // DELETE /tasks/:id
            Method.DELETE / "tasks" / string("id") -> handler { (id: String, _: Request) =>
                TaskRepo
                  .delete(id)
                  .mapBoth(
                      _ => Response.internalServerError(s"Failed to delete task $id"),
                      deleted =>
                          if deleted then
                              Response.text(s"Task $id deleted successfully!")
                          else
                              Response.notFound(s"Task $id not found!")
              )
            }
        )