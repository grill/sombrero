package org.sombrero.widget

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.common._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import java.net._
import net.liftweb.http.js._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

import org.sombrero.snippet._
import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.widget
import _root_.scala.xml._

/**
 * Gnerates a Roomlink widget
 * @author Gabriel Grill
 */
class RoomLink(data: org.sombrero.model.Widget, wp : WidgetPlace) extends widget.Widget(data, "unary", wp) {
   properties ~= ("change", JavaScriptHelper.callback(change)) ~
    ("hoveroff", true)
  if(data.roomlink.image.is != null)
    properties ~= ("img", "/roomlink/" + data.roomlink.id.is.toString + "")
  else
    properties ~= ("img", "/images/roomlink.png")

   override lazy val helpUrl = "/helptext/roomlink"

   def change(): JsCmd = {
     Log.info("From " + id + " change recieved")
     JsRaw("window.location = '/room/" + data.roomlink.room.is + "';").cmd  //redirect in JavaScript
   }
}
