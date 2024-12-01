package tasks
import zio.*
import tasks.Task
import scala.collection.mutable

case class InmemoryTaskRepo(map: Ref[Map[String, Task]]) extends TaskRepo:
    def register(task: Task): UIO[String] =
        for
            id <- Random.nextUUID.map(_.toString)
            _  <- map.update(_ + (id -> task))
        yield id
    
    def update(id: String, updatedTask: Task): UIO[Boolean] =
        for
            exists <- map.get.map(_.contains(id)) // Verifica se a tarefa existe
            result <- if exists then
                map.update(_.updated(id, updatedTask)).as(true)
            else
                ZIO.succeed(false) // Retorna false se a tarefa não existir
        yield result
            
    def lookup(id: String): UIO[Option[Task]] =
        map.get.map(_.get(id))

    def tasks: UIO[List[Task]] =
        map.get.map(_.values.toList)

    def delete(id: String): UIO[Boolean] = // Implementação do método DELETE
        for
            exists <- map.get.map(_.contains(id)) // Verifica se a tarefa existe
            result <- if exists then
                map.update(_ - id).as(true) // Remove a tarefa e retorna true
            else
                ZIO.succeed(false) // Retorna false se a tarefa não existir
        yield result

object InmemoryTaskRepo {
    def layer: ZLayer[Any, Nothing, InmemoryTaskRepo] =
        ZLayer.fromZIO(
            Ref.make(Map.empty[String, Task]).map(new InmemoryTaskRepo(_))
        )
}