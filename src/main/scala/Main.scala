import zio._
import zio.http._
import controller.TaskController
import service.TaskService
import repository.TaskRepository

object Main extends ZIOAppDefault {
    override def run: ZIO[ZIOAppArgs with Scope, Any, Any] = {
        val server = Server.app(TaskController.routes).withPort(8080)
        
        server.make.provide(
            TaskRepository.live, // Camada de persistência
            TaskService.live,    // Camada de serviço
            Server.default       // Servidor HTTP
        ).exitCode
    }
}
