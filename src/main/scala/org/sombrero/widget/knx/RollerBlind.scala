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
 
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception._ 

import org.scalimero.device.preconf

/**
 * Generates a Rollo widget
 * @author Gabriel Grill
 */
class RollerBlind (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget[preconf.RollerBlind.DataPointValueType, preconf.RollerBlind.PrimitiveType](data, "analog", wp){
  val knx = preconf.RollerBlind(data.knx().groupAddress.is)
  override val helpUrl = "/helptext/rollerblind"

  properties ~= ("frontImg" -> "/images/rollo0zu.png") ~
    ("backgroundImg", "/images/rollo0.png") ~
    ("slideRect", JArray(19 :: 19 :: 122 :: 122 :: Nil)) ~
    ("reverse", true) ~
    ("value", try{device.read}catch{case e=>0})

   def translate(value: Int): String = value.toString
   def translate(value: String): Int = if((value.toFloat * 100) < 0) 0 else value.toFloat * 100
}
