package tasks

import java.util.UUID
import java.util.UUID
import zio.json.*
import zio.schema._
import zio.schema.DeriveSchema._

case class Task(
 title: String,         // Título ou nome da tarefa
 description: String,   // Descrição da tarefa
 isCompleted: Boolean,  // Status indicando se a tarefa foi concluída
 // createdAt: java.time.Instant, // Data/hora de criação
 // updatedAt: Option[java.time.Instant] // Data/hora da última atualização (opcional)
)

case class TaskReturn(
 id: UUID,
 title: String, // Título ou nome da tarefa
 description: String, // Descrição da tarefa
 isCompleted: Boolean, // Status indicando se a tarefa foi concluída
 // createdAt: java.time.Instant, // Data/hora de criação
 // updatedAt: Option[java.time.Instant] // Data/hora da última atualização (opcional)
)
object Task:
    given Schema[Task] = DeriveSchema.gen[Task]

object TaskReturn:
    given Schema[TaskReturn] = DeriveSchema.gen[TaskReturn]
     