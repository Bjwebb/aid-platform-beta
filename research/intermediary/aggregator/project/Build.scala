import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "aggregator"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "joda-time" % "joda-time" % "2.2",
    "org.basex" % "basex"     % "7.6"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(
      "BaseX Maven Repository" at "http://files.basex.org/maven",
      "XQL Maven Repository"   at "http://xqj.net/maven"
    )
  )

}
