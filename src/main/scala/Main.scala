import zio._

object Main extends ZIOAppDefault {
    
    // O programa ZIO
    val program: ZIO[Any, Nothing, Unit] =
        ZIO.succeed(println("Hello, World!"))
    
    // Definindo a execução do programa
    override def run: ZIO[Any, Nothing, Unit] =
        program
}

