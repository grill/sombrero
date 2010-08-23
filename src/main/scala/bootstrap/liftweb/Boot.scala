package bootstrap.liftweb

import _root_.net.liftweb.util.Helpers
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._

import org.sombrero.snippet.DButil
import org.sombrero.model._
import org.sombrero.util._
import org.sombrero.comet._
import org.sombrero.view._
import org.sombrero._



/**
  * A class that's instantiated and run on Startup.  It allows the programer
  * to modify lift's environment
  * @author Alexander Steiner and Gabriel Grill
  */
class Boot {
  def boot {
    // set up database connection
    if (!DB.jndiJdbcConnAvailable_?) DB.defineConnectionManager (DefaultConnectionIdentifier, DBVendor)

    // where to search snippet
    LiftRules.addToPackages("org.sombrero")
    LiftRules.jsArtifacts = JQuery14Artifacts

    //org.sombrero.util.Connection.createConnection("172.19.0.7")
    //org.sombrero.comet.SombreroKNXListener.start

    //Add new Resources
    ResourceServer.allow {
      case "js" :: _ => true
      case "css" :: _ => true
      case "plugin" :: _ => true
      case "images" :: _ => true
    }
    val loggedIn = If(() => User.loggedIn_?,
      () => RedirectResponse("/login"))

    //Create Tables
    Schemifier.schemify(true, Schemifier.infoF _, Room, User, Position, Widget, KNXWidget, RoomlinkWidget, Fav, KNXGroup, KNXAlias, KNXRouter)

    // Build SiteMap
   val entries = Menu(Loc("Home", List("index"), "Home")) ::
     Menu(Loc("KNX", ("KNXWidgetForm" :: Nil) -> true, "KNXWidgetForm", Hidden, If(() => true, null)) ) ::
     Menu( Loc("DBtools", List("db"), "DBtools", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("RoomFoo", List("roomadd"), "Add/Remove", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("Discovery", List("discovery"), "Router Discovery", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("Userlist", List("userlist"), "User List", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("Grouplist", List("knxgroups"), "Group List", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("WidgetAdd", List("widgetadd"), "Widget Add", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu( Loc("CloseFrame", List("closeframe"), "Close Frame")) ::
     Menu( Loc("DeviceFinder", List("devicefinder"), "DeviceFinder", Loc.If(User.superUser_? _, () => NotFoundResponse()))) ::
     Menu(Loc("Help", List("helptext") -> true, "Help")) ::
     Menu( new RoomLoc()) ::
     Menu( new WidgetLoc()) ::
     Menu( new UserLoc()) ::
     Menu( new WidgetViewLoc()) :: Nil

    LiftRules.setSiteMap(SiteMap((entries ::: User.sitemap):_*))

  //Custom Dispatch for Room Images
  LiftRules.dispatch.append {
  case Req("room" :: roomid :: "image" :: Nil, _, _) =>
    () => RoomImage.get(roomid)
  case Req("roomlink" :: rlid :: Nil, _ , _) =>
    () => RoomLinkImage.get(rlid)
  }

  try {
    //connection establishment on start up
    KNXRouter.getIP.map(ip => {Log.info(ip); /*util.Connection.createConnection(ip);*/ SombreroNetwork(ip).open})
    //connection establishment on shut down
    //LiftRules.unloadHooks.append(() => { if(org.sombrero.util.Connection.isConnected) org.sombrero.util.Connection.destroyConnection})
    LiftRules.unloadHooks.append(() => { if(SombreroNetwork.open_?) SombreroNetwork.close})
  } catch {
    //matches all exceptions
    case e => Log.info(e.getMessage)
  }

  //Distributor ! TestMessage(1l, "Hi I'm Boot.scala")
}
}

//provides database access
object DBVendor extends ConnectionManager {
  //connect to database
  def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    try {

      Class.forName("org.h2.Driver")
      val dm = DriverManager.getConnection("jdbc:h2:sombrero")
      Full(dm)
    } catch {
      case e : Exception => e.printStackTrace; Empty
    }
  }

  //shutdown database
  def releaseConnection(conn: Connection) = conn.close
}
