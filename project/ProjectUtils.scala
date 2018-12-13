import sbtcrossproject.{CrossPlugin, CrossProject, CrossType}
//import sbtcrossproject.CrossPlugin.autoImport._
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin.autoImport.Revolver
import webscalajs.ScalaJSWeb
import org.scalajs.sbtplugin.ScalaJSPlugin
import play.twirl.sbt.SbtTwirl
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin
import com.lightbend.sbt.SbtAspectj
import com.typesafe.sbt.web.Import._
import spray.revolver.RevolverPlugin
import play.twirl.sbt.SbtTwirl
import sbtbuildinfo.BuildInfoPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport.{BuildInfoKey, buildInfoKeys, buildInfoObject, buildInfoPackage}
import spray.revolver.RevolverPlugin.autoImport.Revolver
import scala.sys.process._
import scala.util.Try
import _root_.io.megl._
//import scala.sys.process._

object ProjectUtils {
  type PE = Project => Project
  type XPE = CrossProject => CrossProject

  def preventPublication: PE =
    _.settings(publishTo := Some(Resolver.file("Unused transient repository",
          target.value / "fakepublish")), publishArtifact := false,
      packagedArtifacts := Map.empty) // doesn't work - https://github.com/sbt/sbt-pgp/issues/42

  def setupJVMProject(path: String, publish: Boolean = true) = {
    val id = generateId(path)
    Project(id = id, file(s"$path"))
      .configure(setupDefaultProject(path, publish))
      .settings(Common.settings)
  }

  def setupMicroservicesProject(path: String, publish: Boolean = true) = {
    val id = generateId(path)
    Project(id = id, file(s"$path"))
      .configure(setupDefaultProject(path, publish))
      .settings(Common.settings)
      .enablePlugins(ProjectServerPackage)
      .settings(
        libraryDependencies ++= Dependencies.loggingSupport ++ Dependencies.httpServiceSupportTest
      )
  }

  def setupJSProject(path: String, publish: Boolean = true) = {
    val id = generateId(path)
    Project(id = id, file(path))
      .enablePlugins(SbtProjectCommon)
      .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
      .configure(setupDefaultProject(path, publish))
      .settings(Common.settingsJS)
  }

  def setupClientAppProject(path: String, publish: Boolean = false) = {
    val id = generateId(path)
    Project(id = id, file(path))
      .enablePlugins(ProjectAppPackager)
      .configure(setupDefaultProject(path, publish))
  }

  def setupDefaultProject(path: String, publish: Boolean = true)(
    project: Project) = {
    val docName = path.split("/").map(_.capitalize).mkString(" ")
    val fullname = s"${Common.appName}-${generateName(path)}"
    project
      .enablePlugins(SbtProjectCommon)
      .settings(
        description := s"${Common.appName.capitalize} $docName",
        moduleName := fullname,
        name := fullname
      )

  }

  private val pathToSkipInNames = Set("libraries", "pocs", "component")

  //.settings(Common.publishSettings)
  private def generateName(path: String): String = {
    path.split("/").filterNot(v => pathToSkipInNames.contains(v)).mkString("-")
  }

  private def generateId(path: String): String = {
    path
      .split("/")
      .filterNot(v => pathToSkipInNames.contains(v))
      .flatMap(_.split("-"))
      .reduce(_ + _.capitalize)
  }

  def setupCrossModule(path: String,
    crossType: sbtcrossproject.CrossType = sbtcrossproject.CrossType.Full,
    publish: Boolean = true) = {
    val id = generateId(path)
    import CrossPlugin.autoImport._
    CrossProject(id, file(path))(scalajscrossproject.JSPlatform,
      sbtcrossproject.JVMPlatform)
      .crossType(crossType)
      .withoutSuffixFor(sbtcrossproject.JVMPlatform)
      .jvmSettings(Common.settings)
      .platformsSettings(scalajscrossproject.JSPlatform)(Common.settingsJS)
      .enablePlugins(SbtProjectCommon)
      .configure(setupDefaultProject(path, publish))

  }

  def enableDebugging: Def.SettingsDefinition =
    if (sys.env.contains("DEBUG"))
      Revolver.enableDebugging(port = 5005, suspend = true)
    else Def.settings()

}
