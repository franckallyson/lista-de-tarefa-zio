ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
      name := "lista-de-tarefas-zio"
  )
  .settings(
      libraryDependencies ++= Seq(
          "dev.zio" %% "zio" % "2.1.13",
          "dev.zio" %% "zio-http" % "0.0.5",
          "dev.zio" %% "zio-json" % "0.6.2",
      )
  )
