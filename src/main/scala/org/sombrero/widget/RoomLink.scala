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

class RoomLink(data: org.sombrero.model.Widget, wp : WidgetPlace) extends widget.Widget(data, "unary", wp) {
   properties ++ Map(
        "click" -> JavaScriptHelper.callback(change),
        if(data.roomlink.image.is != null) ("img" -> ("\"/roomlink/" + data.roomlink.id.is.toString + "\"")) else ("img" -> "\"/images/roomlink.png\""),
        "hoveroff" -> "true"
   )
        
   def change(): JsCmd = {
     println("From " + id + " change recieved")
 
     JsRaw("window.location = 'http://localhost:9090/room/" + data.roomlink.room.is + "';").cmd  //redirect in JavaScript
   }
}
