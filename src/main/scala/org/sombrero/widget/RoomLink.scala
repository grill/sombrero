package org.sombrero.widget

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import _root_.net.liftweb.util.Log
import java.net._
import net.liftweb.http.js._

import org.sombrero.snippet._
import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
<<<<<<< HEAD:src/main/scala/org/sombrero/widget/RoomLink.scala

class RoomLink(data: org.sombrero.model.Widget) extends widget.Widget(data, "unary") {
   properties ++ Map(
        "change" -> JavaScriptHelper.callback(change),
        "img" ->  "\"/images/roomlink.png\"", 
        "hoveroff" -> "true"
=======
  
case class RoomLinkAdmCopy(data: org.sombrero.model.Widget) extends ProtoRoomLink(data, ToolBox.id) {
	properties ::= ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") 
}   
case class RoomLinkFavCopy(data: org.sombrero.model.Widget) extends ProtoRoomLink(data, Fav.htmlid) {
	properties ::= ("is_active", "true")
}
case class RoomLink(data: org.sombrero.model.Widget) extends ProtoRoomLink(data, Container.htmlid)

class ProtoRoomLink(data: org.sombrero.model.Widget, parent:String) extends Widget(data, "RoomLink", "unary", parent){
   var properties = List(  
        ("click", JavaScriptHelper.callback(change)),
        ("img",  "\"/images/roomlink.png\""), 
        ("hoveroff", "true")
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/RoomLink.scala
   )
        
   def change(): JsCmd = {
     println("From " + id + " change recieved")
 
     JsRaw("window.location = 'http://localhost:9090/room/" + data.roomlink.room.is + "';").cmd  //redirect in JavaScript
   }
}