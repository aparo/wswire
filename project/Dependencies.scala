package io.megl

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Keys._
import sbt._

object Dependencies {

  object Version {
    // Modules Libraries
    lazy val circeDerivation = "1.0.0"
    // LIBRARIES
    lazy val scala = "2.12.6"
    lazy val circe = "0.10.0"
    lazy val enumeratumCirce = "1.5.18"
    lazy val logback = "1.2.3"
    lazy val scalaTest = "3.0.5"
    lazy val scalaLoggingVersion = "3.9.0"
    lazy val slf4jVersion = "1.7.25"
    lazy val scalaTime = "2.0.0-M12"
    lazy val scalaMock = "3.6.0"

    // Akka ----------

    object Akka {
      lazy val main = "2.5.17"
      object Http {
        lazy val main = "10.1.5"

        // http extensions
        lazy val json = "1.21.0"
        lazy val sse = "3.8.8"
      }

    }

  }

  object Akka {
    lazy val actor: ModuleID = "com.typesafe.akka" %% "akka-actor" % Version.Akka.main
    lazy val slf4j: ModuleID = "com.typesafe.akka" %% "akka-slf4j" % Version.Akka.main
    lazy val testkit: ModuleID = "com.typesafe.akka" %% "akka-testkit" % Version.Akka.main
    lazy val contrib: ModuleID = "com.typesafe.akka" %% "akka-contrib" % Version.Akka.main
    lazy val remote: ModuleID = "com.typesafe.akka" %% "akka-remote" % Version.Akka.main
    lazy val agent: ModuleID = "com.typesafe.akka" %% "akka-agent" % Version.Akka.main
    lazy val stream: ModuleID = "com.typesafe.akka" %% "akka-stream" % Version.Akka.main
    lazy val streamTestkit: ModuleID = "com.typesafe.akka" %% "akka-stream-testkit" % Version.Akka.main
    lazy val clusterSharding: ModuleID = "com.typesafe.akka" %% "akka-cluster-sharding" % Version.Akka.main

    object Http {
      lazy val base: ModuleID = "com.typesafe.akka" %% "akka-http" % Version.Akka.Http.main
      lazy val core: ModuleID = "com.typesafe.akka" %% "akka-http-core" % Version.Akka.Http.main
      lazy val testkit: ModuleID = "com.typesafe.akka" %% "akka-http-testkit" % Version.Akka.Http.main
      lazy val sprayJson: ModuleID = "com.typesafe.akka" %% "akka-http-spray-json" % Version.Akka.Http.main
      lazy val sse: ModuleID = "de.heikoseeberger" %% "akka-sse" % "3.0.0"
      lazy val session: ModuleID = "com.softwaremill.akka-http-session" %% "core" % "0.5.6"
      lazy val sessionJwt: ModuleID = "com.softwaremill.akka-http-session" %% "jwt" % "0.5.6"
      lazy val swagger: ModuleID = "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.0"
      lazy val cors: ModuleID = "ch.megard" %% "akka-http-cors" % "0.3.0"
    }

    object AkkaHTTPJson {
      lazy val circe = "de.heikoseeberger" %% "akka-http-circe" % Version.Akka.Http.json
    }

  }
  

  object Circe {
    lazy val core = Def.setting("io.circe" %%% "circe-core" % Version.circe)
    lazy val parser = Def.setting("io.circe" %%% "circe-parser" % Version.circe)
    lazy val java8 = Def.setting("io.circe" %%% "circe-java8" % Version.circe)
    lazy val testing =
      Def.setting("io.circe" %%% "circe-testing" % Version.circe)
  }

  object Enumeratum {
    lazy val circe = Def.setting(
        "com.beachape" %% "enumeratum-circe" % Version.enumeratumCirce)

  }

  object Libraries {
    lazy val specs2 = "org.specs2" %% "specs2-core" % "3.9.5"
    lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    lazy val magnolia = Def.setting("com.propensive" %%% "magnolia" % "0.10.0")
    lazy val quickLens =
      Def.setting("com.softwaremill.quicklens" %%% "quicklens" % "1.4.11")
    // Gestione files
    lazy val betterFiles = "com.github.pathikrit" %% "better-files" % "3.5.0"
    //Gestione configurazioni
    lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.10.0"
    //cross RPC
    lazy val autowire = Def.setting("com.lihaoyi" %%% "autowire" % "0.2.6")
    //shapeless
    lazy val shapeless=Def.setting("com.chuusai" %%% "shapeless" % "2.3.3")
    //validatore di schemi
    lazy val jsonSchemaValidator = "com.github.java-json-tools" % "json-schema-validator" % "2.2.10"
    //testing
    lazy val scalactic =
      Def.setting("org.scalactic" %%% "scalactic" % Version.scalaTest)
    lazy val scalaMock = "org.scalamock" %% "scalamock-scalatest-support" % Version.scalaMock
    lazy val mockito = "org.mockito" % "mockito-all" % "1.9.5"
    lazy val scalaJavaTime = Def.setting("io.github.cquiroz" %%% "scala-java-time" % Version.scalaTime)
  }

  object LogBack {
    lazy val elasticsearchAppender = "com.zenvia" % "logback-elasticsearch-appender" % "1.3"
    lazy val logstashAppender = "net.logstash.logback" % "logstash-logback-encoder" % "5.1"
    lazy val classic = "ch.qos.logback" % "logback-classic" % Version.logback
  }
  object Scala {
    lazy val scalaOrganization = "org.scala-lang"
    lazy val compiler = scalaOrganization % "scala-compiler" % Version.scala
    lazy val library = scalaOrganization % "scala-library" % Version.scala
    lazy val reflect = scalaOrganization % "scala-reflect" % Version.scala
    lazy val macroDef = Seq(reflect, compiler % "provided")
    lazy val xml = "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
    lazy val logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
    lazy val java8Compact = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"

  }

  object ScalaTest {
    lazy val test =
      Def.setting("org.scalatest" %%% "scalatest" % Version.scalaTest)
  }

  object TypeSafe {
    lazy val config = "com.typesafe" % "config" % "1.3.3"
  }

  lazy val circeDerivation = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Circe.parser.value,
      Circe.core.value,
      Circe.java8.value,
      Enumeratum.circe.value,
      "org.typelevel" %%% "macro-compat" % "1.1.1"
    ) ++ DependencyHelpers.test(Circe.testing.value)
  }

  lazy val commonUtilsJVM = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Scala.logging,
      TypeSafe.config
    ) ++ DependencyHelpers.provided(Libraries.logback)
  }


  lazy val wsWireCore = Def.settings {
    libraryDependencies ++= Seq(
      Libraries.shapeless.value
    )
  }


  lazy val wsWireServer = Def.settings {
    libraryDependencies ++= Seq(
      Libraries.pureconfig,
      Akka.Http.core,
      Akka.AkkaHTTPJson.circe,
      Libraries.autowire.value
    ) ++ DependencyHelpers.test(
      Akka.Http.testkit
    )
  }

  lazy val wsWireClientJVM = Def.settings {
    libraryDependencies ++= Seq(
      Libraries.pureconfig,
      Akka.Http.core,
      Akka.AkkaHTTPJson.circe,
      Libraries.autowire.value
    ) ++ DependencyHelpers.test(
      Akka.Http.testkit
    )
  }

  lazy val wsWireClientJS = Def.settings {
    libraryDependencies ++= Seq(
      Libraries.autowire.value
    )
  }

  //****************************************
  // Test Dependencies
  //****************************************

  lazy val testSupport = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Libraries.scalaJavaTime.value,
      ScalaTest.test.value,
      Libraries.scalactic.value
    )
  }

  lazy val testSupportJVM = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Scala.logging,
      Libraries.scalaMock,
      Libraries.mockito
    ) ++ loggingSupport
  }

  lazy val loggingSupport = DependencyHelpers.compile(
    Scala.logging
  )


  lazy val httpServiceSupportTest =   DependencyHelpers.test(
    Akka.Http.testkit
  )
}

object Exclusion {
  lazy val slf4jLog4j12 = ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
}

