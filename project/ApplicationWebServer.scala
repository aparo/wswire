import sbt._
import sbt.Keys._
import com.lightbend.sbt.SbtAspectj
import spray.revolver.RevolverPlugin
import spray.revolver.RevolverPlugin.autoImport.Revolver

trait ApplicationKeys {
  val sigarLoader = taskKey[File]("Sigar loader jar file")

  val sigarLoaderOptions =
    taskKey[Seq[String]]("JVM options for the Sigar loader")
}
object ApplicationKeys extends ApplicationKeys

object ProjectApp extends AutoPlugin {
  import RevolverPlugin.autoImport.reStart
  import SbtAspectj.autoImport._
  import ApplicationKeys._
  import _root_.io.megl.EnviromentGlobal._

  override def requires: Plugins = SbtAspectj && RevolverPlugin

  def enableDebugging: Def.SettingsDefinition =
    if (sys.env.contains("DEBUG"))
      Revolver.enableDebugging(port = 5005, suspend = true)
    else Def.settings()

  lazy val defaultServerSettings: Seq[Def.Setting[_]] = Seq(
    parallelExecution in Test := false,
//    sigarLoader := findSigarLoader(update.value),
//    sigarLoaderOptions := Seq(s"-javaagent:${sigarLoader.value.getAbsolutePath}"),
//    sigarLoaderOptions in Test := sigarLoaderOptions.value :+ s"-Dkamon.sigar.folder=${baseDirectory.value / "target" / "native"}",
    baseDirectory in reStart := baseDirectory.value / "target",
//    aspectjVersion in Aspectj := "1.8.10",
//    aspectjSourceLevel in Aspectj := "-1.8",
//    javaOptions in reStart ++= Seq(
//      s"-DAPP_NAME=$appName",
//      s"-Dconfig.file=/etc/${Common.appName}/$appName/application.conf",
//      s"-Dlogback.configurationFile=/etc/${Common.appName}/$appName/logback.xml"
//    ) //++ (aspectjWeaverOptions in Aspectj).value ++ (sigarLoaderOptions in Test).value,
    //javaOptions in Test ++= (sigarLoaderOptions in Test).value,
  )

  override def projectSettings: Seq[Def.Setting[_]] =
    defaultServerSettings ++ enableDebugging

//  private[this] def findSigarLoader(report: UpdateReport) =
//    report.matching(artifactFilter(name = "sigar-loader", `type` = "jar")).head

}
