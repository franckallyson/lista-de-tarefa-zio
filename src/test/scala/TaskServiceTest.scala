import zio._
import service._
import repository._

object TaskServiceTest extends ZIOAppDefault {
    override def run: ZIO[ZIOAppArgs with Scope, Any, Any] = {
        val test = for {
            service <- ZIO.service[TaskService]
            
            // Adiciona algumas tarefas
            task1 <- service.addTask("Learn Scala")
            task2 <- service.addTask("Build a ZIO App")
            _ <- ZIO.debug(s"Added Tasks: ${task1}, ${task2}")
            
            // Lista as tarefas
            tasksBeforeUpdate <- service.listTasks()
            _ <- ZIO.debug(s"Tasks before update: $tasksBeforeUpdate")
            
            // Atualiza a tarefa
            updatedTask <- service.updateTask(1, Some("Learn Advanced Scala"), Some(true))
            _ <- ZIO.debug(s"Updated Task: $updatedTask")
            
            // Lista as tarefas após atualização
            tasksAfterUpdate <- service.listTasks()
            _ <- ZIO.debug(s"Tasks after update: $tasksAfterUpdate")
            
            // Deleta uma tarefa
            deleteSuccess <- service.deleteTask(2)
            _ <- ZIO.debug(s"Task 2 deleted: $deleteSuccess")
            
            // Lista as tarefas após a exclusão
            tasksAfterDelete <- service.listTasks()
            _ <- ZIO.debug(s"Tasks after delete: $tasksAfterDelete")
        } yield ()
        
        test.provideSomeLayer(TaskRepository.live >>> TaskService.live)
    }
}
