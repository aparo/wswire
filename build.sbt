import com.typesafe.sbt.packager.Keys.dockerCommands
import com.typesafe.sbt.packager.docker.Cmd
import _root_.io.megl._
import sbt.Keys.libraryDependencies
import sbtcrossproject.CrossType

name := "wswire"

scalaVersion := Dependencies.Version.scala

organization := "io.megl"

lazy val root = project
  .in(file("."))
  .settings(Common.settings)
  .settings(name := "wswire-root", publishArtifact := false)
  .aggregate(
    wsWireCoreJVM,
    wsWireCoreJS,
    wsWireOpenApiJVM,
    wsWireOpenApiJS
  )

lazy val wsWireCore =
  ProjectUtils
    .setupCrossModule("core", crossType = CrossType.Pure)
    .settings(Dependencies.wsWireCore)
    .dependsOn(testSupport % Test)

lazy val wsWireCoreJVM = wsWireCore.jvm
lazy val wsWireCoreJS = wsWireCore.js
  .settings(Dependencies.wsWireCoreJS)

lazy val wsWireOpenApi =
  ProjectUtils
    .setupCrossModule("openapi", crossType = CrossType.Pure)
    .dependsOn(testSupport % Test)

lazy val wsWireOpenApiJVM = wsWireOpenApi.jvm
lazy val wsWireOpenApiJS = wsWireOpenApi.js


lazy val wsWireServer =
  ProjectUtils
    .setupJVMProject("akka-server")
    .settings(Dependencies.wsWireServer)
    .dependsOn(wsWireCoreJVM, wsWireOpenApiJVM,  testSupportJVM % Test)

/*
lazy val wsWireClient =
  ProjectUtils
    .setupCrossModule("akka-client", crossType = CrossType.Full)
    .jvmSettings(Dependencies.wsWireClientJVM)
    .jvmSettings(Dependencies.wsWireClientJS)
    .dependsOn(wsWireCore, testSupport % Test)
*/

//****************************************
// Test Support Utilities
//****************************************
lazy val testSupport =
  ProjectUtils
    .setupCrossModule("libraries/test-support", crossType = CrossType.Full)
    .settings(Common.noPublishSettings)
    .settings(Dependencies.testSupport)
    .jvmSettings(Dependencies.testSupportJVM)

lazy val testSupportJS = testSupport.js
lazy val testSupportJVM = testSupport.jvm
