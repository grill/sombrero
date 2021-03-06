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

import tuwien.auto.scalimero.device.preconf
import tuwien.auto.scalimero.device._
import tuwien.auto.scalimero.device.dtype.Num8BitUnsigned._
import tuwien.auto.scalimero.device.dtype.translatortype._

/**
 * Generates a Rollo widget
 * @author Gabriel Grill
 */
class RollerBlind (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget(data, "analog", wp){
  val knx = new SimpleDevice(data.knx().groupAddress.is, SCALING)
  try{knx.readRequest()}catch{case e=>}
  override lazy val helpUrl = "/helptext/rollerblind"

  properties ~= ("frontImg" -> "/images/rollo0zu.png") ~
    ("backgroundImg", "/images/rollo0.png") ~
    ("slideRect", JArray(List(19, 19, 122, 122))) ~
    ("reverse", true) /*~
    ("value", try{knx.read}catch{case e=>0})*/

   def knx2jquery(value: String): String = value
   def jquery2knx(value: String): String = knx.dpt.translate(if((value.toFloat * 100) < 0) 0 else (value.toFloat * 100).toInt)
}
