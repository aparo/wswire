import _root_.io.megl.{Dependencies, Publish}
import sbt.Keys._
import sbt._

object Common {

  lazy val appName="wswire"
  lazy val org= "io.megl"

  lazy val sharedSettings=Publish.ossPublishSettings ++ Seq(
    scalaVersion := Dependencies.Version.scala,
    organization := org,
    parallelExecution in Test := false,
    credentials ++= {
      (
  for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    username,
    password
  )
).toSeq
    },
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
    ),
  ) ++ Licensing.settings

  lazy val settings = sharedSettings ++ Seq(
    scalacOptions := Seq(
      "-encoding", "UTF-8",
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-language:postfixOps",
      "-language:existentials",
      "-language:higherKinds",
      "-Yrangepos",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture",
      "-Xexperimental"
    )
  )

  lazy val settingsJS = sharedSettings ++ Seq(
    scalacOptions := Seq(
      "-encoding", "UTF-8",
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-language:postfixOps",
      "-language:existentials",
      "-language:higherKinds",
      "-Yrangepos",
      "-P:scalajs:sjsDefinedByDefault",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture",
      "-Xexperimental"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value)
  )

  lazy val noPublishSettings = Seq(
    skip in publish := true,
    publishArtifact := false
  )
}
