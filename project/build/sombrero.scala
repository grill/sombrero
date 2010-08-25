import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin
import sbt._

class SombreroProject(info: ProjectInfo) extends DefaultWebProject(info) with JRebelWebPlugin
{

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % "2.1-M1" % "compile->default",
    "net.liftweb" %% "lift-util" % "2.1-M1" % "compile->default",
    "net.liftweb" %% "lift-mapper" % "2.1-M1" % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.apache.derby" % "derby" % "10.2.2.0" % "runtime",
    "javax.servlet" % "servlet-api" % "2.5" % "provided",
    "com.h2database" % "h2" % "1.2.138",
    "org.scalatest" % "scalatest" % "1.2",
    "tuwien.auto" % "calimero" % "2.0a4" from "http://github.com/downloads/grill/SCalimero/calimero-2.0a4.jar",
    "tuwien.auto" % "scalimero" % "1.0" from "http://github.com/downloads/grill/SCalimero/scalimero_2.8.0-1.0.jar"
  ) ++ super.libraryDependencies
  
  override def scanDirectories = Nil
  
  // required because Ivy doesn't pull repositories from poms
  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
  val nexusRepo = "nexus" at "https://nexus.griddynamics.net/nexus/content/groups/public"
}
