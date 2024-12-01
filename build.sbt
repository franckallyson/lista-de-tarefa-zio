ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
      name := "lista-de-tarefas-zio"
  )
  .settings(
      libraryDependencies ++= Seq(
          "dev.zio"       %% "zio"              % "2.1.1",
          "dev.zio"       %% "zio-json"         % "0.6.2",
          "dev.zio"       %% "zio-http"         % "3.0.0-RC8",
          "io.getquill"   %% "quill-zio"        % "4.7.0",
          "io.getquill"   %% "quill-jdbc-zio"   % "4.7.0",
          "com.h2database" % "h2"               % "2.2.224",
      )
  )
