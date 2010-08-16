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
import _root_.net.liftweb.util.Log
import _root_.net.liftweb.common._
import java.net._
import net.liftweb.http.js._

import org.sombrero.widget._
  
import tuwien.auto.calimero.dptxlator._

import org.scalimero.device.preconf

/**
 * Generates a Temperature widget
 * @author Gabriel Grill
 */
class Temperature (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget[preconf.Temperature.DataPointValueType, preconf.Temperature.PrimitiveType](data, "analog", wp){
  val knx = preconf.Temperature(data.knx().groupAddress.is)
   val min:Float = 15
   val max:Float = 30
   
  override val helpUrl = "/helptext/temperature"
  properties ~= ("clip_front", true) ~
    ("value", try{device.read}catch{case e=>0})

  def translate(value: Float): String = (((value-min)/(max-min))*100).toString
  def translate(value: String): Float = if((value.toFloat * 100) < 0) min else (min+((max-min) * value.toFloat)).toFloat
} 