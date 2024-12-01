package tasks

import zio.*
import zio.http.*
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import tasks.Task
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
                                  .internalServerError(s"Failed to register the user: $u"),
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
                          _ => Response.internalServerError(s"Cannot retrieve user $id"),
                          {
                              case Some(task) =>
                                  Response(body = Body.from(task))
                              case None =>
                                  Response.notFound(s"User $id not found!")
                          }
                      )
            },
            // GET /tasks
            Method.GET / "tasks" -> handler {
                TaskRepo.tasks.mapBoth(
                    _ => Response.internalServerError("Cannot retrieve users!"),
                    tasks => Response(body = Body.from(tasks))
                )
            }
        )