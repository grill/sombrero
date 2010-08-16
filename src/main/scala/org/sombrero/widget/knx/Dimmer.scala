package org.sombrero.widget.knx

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import net.liftweb.http.js._

import java.net._

import scala.util.Random
import scala.xml._

import tuwien.auto.calimero.exception._ 
import tuwien.auto.calimero.dptxlator._

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._

import org.sombrero.device.preconf

/**
 * Generates a Dimmer widget
 * @author Gabriel Grill
 */
class Dimmer(data: org.sombrero.model.Widget, wp: WidgetPlace) extends StateWidget(data, "analog", wp){
  val device = preconf.Dimmer(data.knx().groupAddress.is)

  override val helpUrl = "/helptext/dimmer"

  properties ++ Map(
    "frontImg"      -> "\"/images/dim0drag.png\"",
    "backgroundImg" -> "\"/images/dim0.png\"",
    "slideRect"     -> "[19, 90, 122, 42]",
    "opacity"       -> "\"/images/dim0light.png\"",
    "value"         -> (try{device.read}catch{case e=>0}).toString
  )

   def translate(value: Int): String = value.toString
   def translate(value: String): Int = if((value.toFloat * 100) < 0) 0 else value.toFloat * 100
}