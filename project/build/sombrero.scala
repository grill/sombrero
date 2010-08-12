import sbt._

class ProjectNameProject(info: ProjectInfo) extends DefaultWebProject(info)
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
    "org.scalatest" % "scalatest" % "1.2"
  ) ++ super.libraryDependencies
  
  // required because Ivy doesn't pull repositories from poms
  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
  val nexusRepo = "nexus" at "https://nexus.griddynamics.net/nexus/content/groups/public"
}
