ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "lista-de-tarefas-zio"
  )

libraryDependencies += "dev.zio" %% "zio" % "2.1.13"