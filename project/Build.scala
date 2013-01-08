import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "bus"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "org.xerial" % "sqlite-jdbc" % "3.6.16",
      "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
      "io.backchat.jerkson" % "jerkson_2.9.2" % "0.7.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
      testOptions in Test := Nil
    )

}
