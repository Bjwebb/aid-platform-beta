import sbt._
import sbt.Keys._

object BaseXServerBuild extends Build {
  lazy val basexloader = Project("basex-server", file(".")).settings(
    resolvers ++= Seq(
      "BaseX Maven Repository" at "http://files.basex.org/maven",
      "XQL Maven Repository"   at "http://xqj.net/maven"
    ),
    libraryDependencies ++= Seq(
      "org.basex" % "basex" % "7.6",
      "org.basex" % "basex-api" % "7.6",
      "org.eclipse.jetty" % "jetty-server" % "7.6.10.v20130312",
      "org.eclipse.jetty" % "jetty-webapp" % "7.6.10.v20130312"
    )
  )
}
