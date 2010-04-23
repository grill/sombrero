package bootstrap.liftweb

import _root_.net.liftweb.util._
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
import org.sombrero._



/**
  * A class that's instantiated and run on Startup.  It allows the programer
  * to modify lift's environment
  */
class Boot {
  def boot {
    // set up database connection
    if (!DB.jndiJdbcConnAvailable_?) DB.defineConnectionManager (DefaultConnectionIdentifier, DBVendor)
  
    // where to search snippet
    LiftRules.addToPackages("org.sombrero")
    LiftRules.jsArtifacts = JQueryArtifacts

    //org.sombrero.util.Connection.createConnection("172.19.0.7")
    //org.sombrero.comet.SombreroKNXListener.start
    
    //Add new Resources
    ResourceServer.allow {
      case "framework_js" :: _ => true
      case "layout_css" :: _ => true
      case "plugin" :: _ => true 
      case "widget" :: _ => true
      case "widgettheme_css" :: _ => true
    }
    val loggedIn = If(() => User.loggedIn_?,
    		() => RedirectResponse("/login"))
     
    //Create Tables
    Schemifier.schemify(true, Log.infoF _, Room, User, Position, Widget, KNXWidget, RoomlinkWidget, ContainerWidget, Fav)
    
    // Build SiteMap
   val entries = Menu(Loc("Home", List("index"), "Home")) ::
     Menu(Loc("KNX", ("KNXWidgetForm" :: Nil) -> true, "KNXWidgetForm", Hidden, If(() => true, null)) ) ::
     Menu( Loc("DBtools", List("db"), "DBtools")) ::
     Menu( Loc("RoomFoo", List("roomadd"), "Add/Remove")) ::
     Menu( Loc("Discovery", List("discovery"), "Router Discovery")) ::
     Menu( new RoomLoc()) ::
     Menu( new WidgetLoc()) :: Nil
    LiftRules.setSiteMap(SiteMap((entries ::: User.sitemap):_*))
    
  //Custom Dispatch for Room Images
  LiftRules.dispatch.append {
  case Req("room" :: roomid :: "image" :: Nil, _, _) =>
    () => RoomImage.get(roomid)
  } 
  //util.Connection.createConnection("172.19.0.7")
  LiftRules.unloadHooks.append(() => { if(org.sombrero.util.Connection.isConnected) org.sombrero.util.Connection.destroyConnection }) }
  
  //Distributor ! TestMessage(1l, "Hi I'm Boot.scala")
}  

object DBVendor extends ConnectionManager {
  def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    try {

      Class.forName("org.h2.Driver")
      val dm = DriverManager.getConnection("jdbc:h2:sombrero")
      Full(dm)
    } catch {
      case e : Exception => e.printStackTrace; Empty
    }
  }
  def releaseConnection(conn: Connection) = conn.close
}
