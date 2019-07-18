val finchVersion = "0.26.0"
val circeVersion = "0.10.1"
val scalatestVersion = "3.0.5"

mainClass in (Compile,run) := Some("net.masilva.Server")

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

lazy val root = (project in file("."))
  .settings(
    organization := "net-masilva",
    name := "coding-challenge",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    mainClass := Some("net.masilva.Server"),
    libraryDependencies ++= Seq(
      "com.github.finagle" %% "finchx-core"  % finchVersion,
      "com.github.finagle" %% "finchx-circe"  % finchVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.scalatest"      %% "scalatest"    % scalatestVersion % "test",
      "com.typesafe.slick" %% "slick" % "3.3.1",
      "org.slf4j" % "slf4j-nop" % "1.7.26",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.1",
      "com.h2database" % "h2" % "1.4.197" 
    )
  )