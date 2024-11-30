import zio._
import repository._

object TaskRepositoryTest extends ZIOAppDefault {
    override def run: ZIO[ZIOAppArgs with Scope, Any, Any] = {
        val test = for {
            repo <- ZIO.service[TaskRepository]
            _ <- repo.add(Task(1, "Learn Scala", isDone = false))
            _ <- repo.add(Task(2, "Build a ZIO App", isDone = false))
            tasks <- repo.list()
            _ <- ZIO.debug(s"Tasks: $tasks")
            updated <- repo.update(1, Some("Learn Advanced Scala"), Some(true))
            _ <- ZIO.debug(s"Updated Task: $updated")
            _ <- repo.delete(2)
            remaining <- repo.list()
            _ <- ZIO.debug(s"Remaining Tasks: $remaining")
        } yield ()
        
        test.provide(TaskRepository.live)
    }
}

