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

    def lookup(id: String): UIO[Option[Task]] =
        map.get.map(_.get(id))

    def tasks: UIO[List[Task]] =
        map.get.map(_.values.toList)

object InmemoryTaskRepo {
    def layer: ZLayer[Any, Nothing, InmemoryTaskRepo] =
        ZLayer.fromZIO(
            Ref.make(Map.empty[String, Task]).map(new InmemoryTaskRepo(_))
        )
}