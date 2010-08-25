
package org.sombrero.widget.knx

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import java.net._
import net.liftweb.http.js._
import tuwien.auto.calimero.exception._ 
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

import tuwien.auto.calimero.dptxlator._
import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._

import tuwien.auto.scalimero.device.preconf
import tuwien.auto.scalimero.device._
import tuwien.auto.scalimero.device.dtype.Num8BitUnsigned._
import tuwien.auto.scalimero.device.dtype.translatortype._

/**
 * Generates a Dimmer widget
 * @author Gabriel Grill
 */
class Dimmer(data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget(data, "analog", wp){
  override val knx = new SimpleDevice(data.knx().groupAddress.is, SCALING)
  try{knx.readRequest()}catch{case e=>}
  override lazy val helpUrl = "/helptext/dimmer"

  properties ~= ("frontImg",  "/images/dim0drag.png") ~
    ("backgroundImg", "/images/dim0.png") ~
    ("slideRect", JArray(List(19, 90, 122, 42))) ~
    ("opacity", "/images/dim0light.png") /* ~
    ("value", try{knx.read}catch{case e=>0})*/

   def knx2jquery(value: String): String = value
   def jquery2knx(value: String): String = knx.dpt.translate((if((value.toFloat * 100) < 0) 0 else (value.toFloat * 100).toInt))
}