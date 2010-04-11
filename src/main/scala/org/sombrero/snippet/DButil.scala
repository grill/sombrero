package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import java.net._
import _root_.net.liftweb.mapper._

//object DButil {
    
/*object DButil {
>>>>>>> 1.9
    def realcreate() : Unit = {
      Schemifier.schemify(true, Log.infoF _, Room, User, Widget, KNXWidget)
      Schemifier.destroyTables_!!(Log.infoF _, Room, User, Widget, KNXWidget)
      Schemifier.schemify(true, Log.infoF _, Room, User, Widget, KNXWidget)
      Log.info("tables created!")
    }
}*/

class DButil {
  def create() : NodeSeq = {
    def realcreate() : Unit = {
      Schemifier.schemify(true, Log.infoF _, Room, User, Position, Widget, KNXWidget, RoomlinkWidget, ContainerWidget, Fav)
      Schemifier.destroyTables_!!(Log.infoF _, Room, User, Position, Widget, KNXWidget, RoomlinkWidget, ContainerWidget, Fav)
      Schemifier.schemify(true, Log.infoF _, Room, User, Position, Widget, KNXWidget, RoomlinkWidget, ContainerWidget, Fav)
      Log.info("tables created!")
    }
    link("/db", () => realcreate, Text("create the tables!"))
  }
  
   
  def insert() : NodeSeq = {
    def realinsert() : Unit = {
      var r = Room.create.name("living room").saveMe
      val wlamp = Widget.create.wclass("Lamp").name("Door Lamp").room(r).saveMe
      wlamp.knx.groupAddress("1/1/1").remoteHost("172.19.0.7").saveMe
      Widget.create.wclass("Temperature").name("Temperature").room(r).saveMe.knx.groupAddress("3/4/5").remoteHost("172.19.0.7").save
      val wsw1 = Widget.create.wclass("SwitchOn").name("On-Switch").room(r).saveMe
      wsw1.knx.groupAddress("0/0/1").remoteHost("192.168.0.100").saveMe
      val wsw2 = Widget.create.wclass("SwitchOff").name("Off-Switch").room(r).saveMe
      wsw2.knx.groupAddress("0/0/2").remoteHost("192.168.0.100").saveMe
      r = Room.create.name("kitchen").saveMe
      Widget.create.wclass("Lamp").name("Window Lamp").room(r).saveMe.knx.groupAddress("1/1/1").remoteHost("172.19.0.7").save
      Widget.create.wclass("Switch").name("Switch").room(r).saveMe.knx.groupAddress("0/0/3").remoteHost("192.168.0.100").save
      val rkitchen = r
      r = Room.create.name("freezer").parent(r).saveMe
      Widget.create.wclass("Temperature").name("Temperature").room(r).saveMe.knx.groupAddress("3/4/5").remoteHost("1.2.3.4").save
      Widget.create.wclass("Rollo").name("Window").room(r).saveMe.knx.groupAddress("3/4/5").remoteHost("1.2.3.4").save
      Widget.create.wclass("Dimmer").name("Window Lamp").room(r).saveMe.knx.groupAddress("3/4/5").remoteHost("1.2.3.4").save
      Widget.create.wclass("Roomlink").name("Kitchen").room(r).saveMe.roomlink.room(rkitchen).save
      Log.info("test data inserted!")
    }
    link("/db", () => realinsert, Text("insert testdata!"))
  }
}
