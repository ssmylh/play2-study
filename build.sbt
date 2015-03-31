lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "play2-sample",
    version := "0.1",
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      "com.h2database" % "h2" % "1.4.+",
      "org.scalikejdbc" %% "scalikejdbc" % "2.2.+",
      "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.+",
      "org.scalikejdbc" %% "scalikejdbc-play-plugin" % "2.3.+",
      "com.github.tototoshi" %% "play-flyway" % "1.2.1",
      "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.+"  % "test",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test"
    )
  )
