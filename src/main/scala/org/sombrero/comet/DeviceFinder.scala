//author: Alexander C. Steiner
package org.sombrero.comet

import tuwien.auto.calimero.process._
import tuwien.auto.calimero.DetachEvent
import org.sombrero.model.KNXGroup
import org.sombrero.model.KNXWidget
import org.sombrero.util.Connection
import net.liftweb.http.js.{JsCmd,JsCmds}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.net.liftweb.http._
import SHtml._
import JsCmds._ // For implicits
import scala.xml._
import scala.concurrent.ops._
import org.sombrero.snippet.Widgetadd
import org.sombrero.util.JavaScriptHelper

case object Clear

//find devices by listening on the KNX network
class DeviceFinder extends ProcessListener with CometActor{
  override def defaultPrefix = Full("df")
  
  var devs : Map[String, Int] = Map[String,Int]() withDefaultValue 0

  override def groupWrite(e : ProcessEvent) : Unit =
    this ! e.getDestination.toString
  
  override def detached(e : DetachEvent) : Unit = {}
  
  def render = bind("entries" -> <table id="dftable" />,
    "button" -> ajaxButton("Start Device Finder", () => {this ! Clear; Noop}))
    
  override def lowPriority : PartialFunction[Any, Unit] = {
  case dev : String => {
      devs = devs.updated(dev, devs(dev) + 1)
      doUpdate
    }
    case Clear => {
      devs = Map[String,Int]() withDefaultValue 0
      Connection.knxComm.addProcessListener(this)
      doUpdate
    }
  }
  
  def doUpdate = partialUpdate(SetHtml("dftable",
          <table id="dftable">
            <tr><td>Device</td><td>Count</td></tr>
            {devs.filter((a) => a._2 >= 3).foldLeft(Nil : NodeSeq)
              {(a,b) => {
                val newKNX = Full(KNXWidget.create.groupAddress(b._1))
                a ++
                <tr>
                  <td><a href={"/widgetadd?groupAddress="+b._1} class="dflink" id={"knxaddr" + b._1}>{Text(b._1)}</a></td>
                  <td>{b._2}</td>
                </tr>}
            }}
          </table>) & JavaScriptHelper.popupCmd("dflink", "Add Widget"))
}