addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.12")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt-coursier" % "1.15")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")

addSbtPlugin("com.softwaremill.clippy" % "plugin-sbt" % "0.5.3")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

//Akka Http
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
//addSbtPlugin("com.dwijnand"      % "sbt-reloadquick"  % "1.0.0")

addSbtPlugin("com.lightbend.sbt" % "sbt-aspectj" % "0.11.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.0.0")

//FrontEnd JS
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "0.6.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.25")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.13.1")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.13.1")

//Template rendering
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.3.15")

// Server side plugins
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.lightbend.sbt" % "sbt-aspectj" % "0.11.0")

// Code generators
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")