package org.sombrero.widget.knx


import _root_.net.liftweb.http._
import S._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds, JsExp}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.util.Helpers
import _root_.net.liftweb.common._
import tuwien.auto.calimero.exception._ 

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._
 
import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception._ 

import org.scalimero.device.preconf

/**
 * Generates a Lamp widget
 * @author Gabriel Grill
 */
class Lamp (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget[preconf.Lamp.DataPointValueType, preconf.Lamp.PrimitiveType](data, "binary", wp){
  override val knx = preconf.Lamp(data.knx().groupAddress.is)

  override val helpUrl = "/helptext/lamp"

  properties ~= ("value", try{device.read}catch{case e=>false})

  def translate(value: Boolean): String = value.toString
  def translate(value: String): Boolean = ! value.toBoolean
}