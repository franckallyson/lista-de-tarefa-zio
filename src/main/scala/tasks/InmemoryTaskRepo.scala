/*
Para usar o InMemory, é necessário configurar o objeto TaskReturn, 
pois este retorna o ID para o client_side para que ele possa realizar 
as solicitações para o back. 

package tasks
import zio.*
import tasks.Task
import scala.collection.mutable

case class InmemoryTaskRepo[T](map: Ref[Map[String, T]]) extends TaskRepo:
    def register(task: T): UIO[String] =
        for
            id <- Random.nextUUID.map(_.toString)
            _  <- map.update(_ + (id -> task))
        yield id
    
    def update(id: String, updatedTask: T): UIO[Boolean] =
        for
            exists <- map.get.map(_.contains(id)) // Verifica se a tarefa existe
            result <- if exists then
                map.update(_.updated(id, updatedTask)).as(true)
            else
                ZIO.succeed(false) // Retorna false se a tarefa não existir
        yield result
            
    def lookup(id: String): UIO[Option[T]] =
        map.get.map(_.get(id))

    def tasks: UIO[List[T]] =
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
    def layer[T]: ZLayer[Any, Nothing, InmemoryTaskRepo[T]] =
        ZLayer.fromZIO(
            Ref.make(Map.empty[String, T]).map(new InmemoryTaskRepo(_))
        )
}
 */
