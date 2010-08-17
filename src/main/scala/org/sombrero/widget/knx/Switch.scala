package org.sombrero.widget.knx

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

import org.sombrero.util._
import org.sombrero.util.Log
import org.sombrero.model._
import org.sombrero.model
import org.sombrero.snippet._
import org.sombrero.widget._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception.KNXException._
import tuwien.auto.calimero.link._

import org.scalimero.device.preconf
import org.scalimero.device._
import org.scalimero.device.dtype.Boolean._
import org.scalimero.device.dtype.translatortype._
 
/**
 * Generates a Unary Widget
 * @author Gabriel Grill
 */
class Unary (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  CommandWidget(data, "unary", wp){
  val knx = new SimpleDevice(data.knx.groupAddress.is, BOOLEAN, TRIGGER)
  knx.readRequest()
  override lazy val helpUrl = "/helptext/switch"

  def jquery2knx(value: String): String = value
}

//Generates a Switch widget
class Switch (data: model.Widget, wp: WidgetPlace) extends Unary(data, wp) {
  properties ~= ("img", "/images/Toggle.png")
}

//Generates a SwitchOff widget
class SwitchOff (data: org.sombrero.model.Widget, wp: WidgetPlace) extends Unary(data, wp){  
  properties ~= ("img", "/images/ButtonOff.png")
}
 
//Generates a SwitchOn widget
class SwitchOn (data: org.sombrero.model.Widget, wp: WidgetPlace) extends Unary(data, wp){ 
  properties ~= ("img", "/images/ButtonOn.png")  
}
