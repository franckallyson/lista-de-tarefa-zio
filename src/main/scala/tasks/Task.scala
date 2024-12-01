package tasks

import java.util.UUID
import zio.json.*
import zio.schema._
import zio.schema.DeriveSchema._

case class Task(name: String, description: String, completed: Boolean )

object Task:
    given Schema[Task] = DeriveSchema.gen[Task]