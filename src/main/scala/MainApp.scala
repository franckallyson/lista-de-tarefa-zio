
import tasks.{
    //InmemoryTaskRepo,
    PersistentTaskRepo,
    TaskRoutes
}
import zio._
import zio.http._
import zio.http.Header.{AccessControlAllowOrigin, Origin}
import zio.http.Middleware.{CorsConfig, cors}

object MainApp extends ZIOAppDefault:
    val config: CorsConfig =
        CorsConfig(
            allowedOrigin = {
                case origin if origin == Origin.parse("http://localhost:63342").toOption.get =>
                    Some(AccessControlAllowOrigin.Specific(origin))
                case _ => None
            },
        )
    def run =
        Server
          .serve(
              TaskRoutes() @@ cors(config) // Aplica o Middleware de Cors
          )
          .provide(
              Server.defaultWithPort(8080),

              // To use the persistence layer, provide the `PersistentTaskRepo.layer` layer instead
              PersistentTaskRepo.layer
          )