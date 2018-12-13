package io.megl

import sbt.Def
import spray.revolver.RevolverPlugin.autoImport.Revolver

object EnviromentGlobal {

  def appName: String =
    if (sys.env.contains("APP_NAME")) sys.env("APP_NAME") else "wswire"

}
