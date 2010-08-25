package org.sombrero.widget.knx

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
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
import _root_.net.liftweb.common._
import java.net._
import net.liftweb.http.js._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

import org.sombrero.widget._
import org.sombrero.util.Log
  
import tuwien.auto.calimero.dptxlator._

import tuwien.auto.scalimero.device.preconf
import tuwien.auto.scalimero.device._
import tuwien.auto.scalimero.device.dtype.Num2ByteFloat._
import tuwien.auto.scalimero.device.dtype.translatortype._

/**
 * Generates a Temperature widget
 * @author Gabriel Grill
 */
class Temperature (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget(data, "analog", wp){
  val knx = new SimpleDevice(data.knx().groupAddress.is, TEMPERATURE)
   val min:Float = 15
   val max:Float = 30
  try{knx.readRequest()}catch{case e=>}
   
  override lazy val helpUrl = "/helptext/temperature"
  properties ~= ("clip_front", true)

  def knx2jquery(value: String): String = knx.dpt.translate(((knx.dpt.translate(value)-min)/(max-min))*100)
  def jquery2knx(value: String): String = knx.dpt.translate(if((value.toFloat * 100) < 0) min else (min+((max-min) * value.toFloat)).toFloat)
}