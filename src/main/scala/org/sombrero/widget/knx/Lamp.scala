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
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._
 
import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception._ 

import org.scalimero.device.preconf
import org.scalimero.device._
import org.scalimero.device.dtype.Boolean._
import org.scalimero.device.dtype.translatortype._

/**
 * Generates a Lamp widget
 * @author Gabriel Grill
 */
class Lamp (data: org.sombrero.model.Widget, wp: WidgetPlace) extends
  StateWidget(data, "binary", wp){
  override val knx = new SimpleDevice(data.knx().groupAddress.is, BOOLEAN, SWITCH)
  try{knx.readRequest()}catch{case e=>}
  override lazy val helpUrl = "/helptext/lamp"

//  properties ~= ("value", try{knx.read}catch{case e=>false})

  def knx2jquery(value: String): String = value
  def jquery2knx(value: String): String = knx.dpt.translate(! value.toBoolean)
}