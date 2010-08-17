package org.sombrero.widget.knx

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero._

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
import _root_.net.liftweb.common._
import java.net._
import net.liftweb.http.js._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
 
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception._ 

import org.scalimero.device.preconf
import org.scalimero.device._

/**
 * Generates a Rollo widget
 * @author Gabriel Grill
 */
class RollerBlind (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget(data, "analog", wp){
  val knx = new SimpleDevice(data.knx().groupAddress.is, NUM8BIT_UNSIGNED, SCALING)
  knx.readRequest()
  override val helpUrl = "/helptext/rollerblind"

  properties ~= ("frontImg" -> "/images/rollo0zu.png") ~
    ("backgroundImg", "/images/rollo0.png") ~
    ("slideRect", JArray(List(19, 19, 122, 122))) ~
    ("reverse", true) /*~
    ("value", try{knx.read}catch{case e=>0})*/

   def translate(value: Array[Byte]): String = knx.dpt.translate(value)
   def translate(value: String): String = knx.dpt.translate(if((value.toFloat * 100) < 0) 0 else (value.toFloat * 100).toInt)
}
