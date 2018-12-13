import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.sbt.SbtNativePackager.autoImport.packageName
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.{dockerBaseImage, dockerRepository}
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport.defaultLinuxInstallLocation
import sbt.Keys._
import sbt._
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.MappingsHelper.directory
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport.{bashScriptConfigLocation, bashScriptExtraDefines}
import com.typesafe.sbt.packager.archetypes.{JavaAppPackaging, JavaServerAppPackaging}
import com.typesafe.sbt.packager.docker.{Cmd, DockerPlugin}
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport.{daemonGroup, daemonUser}
import com.typesafe.sbt.packager.rpm.RpmPlugin.autoImport.{rpmGroup, rpmLicense, rpmRelease, rpmUrl, rpmVendor}
import com.lightbend.sbt.SbtAspectj
import sbtbuildinfo.BuildInfoPlugin
import spray.revolver.RevolverPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import spray.revolver.RevolverPlugin.autoImport.Revolver

import scala.sys.process._
import scala.util.Try

trait ProjectPackagerKeys {
  val extraJvmParams = settingKey[Seq[String]]("Extra JVM parameters")
}

object ProjectPackagerKeys extends ProjectPackagerKeys

abstract class ProjectPackager extends AutoPlugin {

  protected val linuxHomeLocation = s"/opt/${Common.appName}"

  override def requires: Plugins = DockerPlugin

  protected val defaultJvmParams = Seq(
    "-Dconfig.file=${app_home}/../conf/application.conf",
    "-Dlogback.configurationFile=${app_home}/../conf/logback.xml"
  )

  protected lazy val defaultPackagingSettings: Seq[Def.Setting[_]] = Seq(
    //ProjectPackagerKeys.extraJvmParams := defaultJvmParams,
    bashScriptExtraDefines ++= ProjectPackagerKeys.extraJvmParams.value.map(p => s"""addJava "$p""""),
    maintainer in Docker := "Alberto Maria Angelo Paro",
    dockerRepository := Some(Common.appName),
    dockerUpdateLatest := true,
    dockerExposedVolumes := Seq(
      s"$linuxHomeLocation/conf"
    ),
    defaultLinuxInstallLocation in Docker := linuxHomeLocation,
    dockerCommands ++= Seq(
      Cmd("ENV", "APPLICATION_HOME", linuxHomeLocation)
    ),
    daemonUser in Docker := "root", // force usage of root user to run the docker entry point
    packageName := name.value,
    packageName in Universal := s"${moduleName.value}-${version.value}",
    executableScriptName := moduleName.value
  )

}

object ProjectAppPackager extends ProjectPackager {

  val autoImport = ProjectPackagerKeys

  override def requires: Plugins = super.requires && JavaAppPackaging

  override lazy val projectSettings = defaultPackagingSettings

}

object ProjectServerPackage extends ProjectPackager {

  val autoImport = ProjectPackagerKeys

  def enableDebugging: Def.SettingsDefinition =
    if (sys.env.contains("DEBUG")) Revolver.enableDebugging(port = 5005, suspend = true) else Def.settings()

  import autoImport._

  override def requires: Plugins = super.requires && JavaServerAppPackaging && ProjectApp && SbtAspectj && RevolverPlugin && BuildInfoPlugin

  override def projectSettings: Seq[Def.Setting[_]] = defaultServerSettings ++ enableDebugging //++ initLogBack


  lazy val defaultServerSettings: Seq[Def.Setting[_]] =  defaultPackagingSettings ++ buildInfoSettings ++ Seq(
        // mappings in Universal ++= Seq(
        //   (aspectjWeaver in Aspectj).value.get -> "bin/aspectjweaver.jar",
        //   sigarLoader.value -> "bin/sigar-loader.jar"
        // ),
    extraJvmParams := defaultJvmParams ++ Seq(
          "-javaagent:${app_home}/aspectjweaver.jar",
          "-javaagent:${app_home}/sigar-loader.jar"
        )
    ,
    dockerExposedVolumes ++= Seq(
      s"$linuxHomeLocation/resolver/cache",
      s"$linuxHomeLocation/resolver/local",
      "/mnt"
    ),
    packageName in Docker := "wswire/" + name.value,
    //    version in Docker     := shortCommit,
    dockerBaseImage := "airdock/oracle-jdk:jdk-1.8",
    defaultLinuxInstallLocation in Docker := s"/opt/${name.value}", // to have consistent directory for files
    daemonUser in Linux := "root",
    daemonGroup in Linux := "root",
    mappings in Universal ++= directory(s"${Common.appName}-${name.value}-service/config"),
    maintainer in Linux := "Alberto Paro <alberto.paro@gmail.com>",
    packageSummary in Linux := s"$description Server",
    packageDescription in Linux := s"$description Server",
    bashScriptConfigLocation := Some(s"/etc/${Common.appName}/conf/jvmopts"),
    // rpm specific
    rpmRelease := "1",
    rpmVendor := "paroconsulting",
    rpmGroup := Some("Servers/Microservices"),
    rpmUrl := Some(s"http://paroconsulting.com"),
    rpmLicense := Some(s"${Common.appName.capitalize}")
  )

  private def buildInfoSettings: Seq[Def.Setting[_]] = Seq(
    buildInfoPackage := "wswire.version",
    buildInfoOptions += BuildInfoOption.Traits("wswire.version.VersionTrait"),
    buildInfoObject := "BuildInfo",
    buildInfoKeys := Seq[BuildInfoKey](
      name, version, scalaVersion, sbtVersion,
      BuildInfoKey.action("buildDate")(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())),
      // if the build is done outside of a git repository, we still want it to succeed
      BuildInfoKey.action("buildSha")(Try("git rev-parse HEAD".!!.stripLineEnd).getOrElse("?"))//sbt 1.x
      //      BuildInfoKey.action("buildSha")(Try(sbt.Process("git rev-parse HEAD").!!.stripLineEnd).getOrElse("?"))
    )
  )

  private[this] def findSigarLoader(report: UpdateReport) =
    report.matching(artifactFilter(name = "sigar-loader", `type` = "jar")).head


  private def readAllText(file: File) = {
    new String(Files.readAllBytes(file.toPath), "UTF-8")
  }


  private def initLogBack:Seq[Def.Setting[_]]=Seq{
    sourceGenerators in Compile += Def.task {
      import scala.sys.process._
      val myLogBack = sourceDirectory.value / "main" / "resources" / "logback.xml"
      val myLogBackTemplate = baseDirectory.value / ".." / "templates" / "logback.xml"
      val myLogBackTemplateCode=readAllText(myLogBackTemplate)
        .replace("|NAME|", name.value)
        .replace("|APPNAME|", Common.appName)
        .replace("|VERSION|", version.value)


      if(myLogBack.exists()){
        val myLogBackCode=readAllText(myLogBack)
        if(myLogBackTemplateCode!=myLogBackCode)
          IO.write(myLogBack, myLogBackTemplateCode.getBytes("UTF-8"))
      } else {
        IO.write(myLogBack, myLogBackTemplateCode.getBytes("UTF-8"))
      }

      Nil

    }.taskValue
  }
}
