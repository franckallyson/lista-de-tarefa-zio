
import tasks.{
    InmemoryTaskRepo,
    PersistentTaskRepo,
    TaskRoutes
}
import zio._
import zio.http._

object MainApp extends ZIOAppDefault:
    def run =
        Server
          .serve(
              TaskRoutes()
          )
          .provide(
              Server.defaultWithPort(8080),

              // An layer responsible for storing the state of the `counterApp`
              //ZLayer.fromZIO(Ref.make(0)),

              // To use the persistence layer, provide the `PersistentUserRepo.layer` layer instead
              InmemoryTaskRepo.layer
          )