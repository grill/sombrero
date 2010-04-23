package org.sombrero.comet

import tuwien.auto.calimero.process._
import tuwien.auto.calimero.DetachEvent
import org.sombrero.model.KNXGroup
import org.sombrero.model.KNXWidget
import org.sombrero.util.Connection
//import net.liftweb.mapper._
import net.liftweb.http.js.{JsCmd,JsCmds}
import _root_.net.liftweb.util._
import Helpers._
import _root_.net.liftweb.http._
import SHtml._
import JsCmds._ // For implicits
import scala.xml._
import scala.concurrent.ops._



class DeviceFinder extends ProcessListener with CometActor{
  override def defaultPrefix = Full("df")
  Connection.knxComm.addProcessListener(this)
  
  var devs : Map[String, Int] = Map[String,Int]() withDefaultValue 0

  override def groupWrite(e : ProcessEvent) : Unit =
    this ! e.getDestination.toString
  
  override def detached(e : DetachEvent) : Unit = {}
  
  def render = bind("results" -> <table id="dftable" />,
    "button" -> ajaxButton("Start Device Finder", () => {devs = Map[String,Int]() withDefaultValue 0; Noop}))
    
  override def lowPriority : PartialFunction[Any, Unit] = {
  case dev : String => {
      devs = devs(dev) += 1
      partialUpdate(SetHtml("dftable",
          <table id="dftable">
            <tr><td>Device</td><td>Count</td></tr>
            {devs.foldLeft(Nil : NodeSeq)
              {(a,b) => a ++ <tr><td>{b._1}</td><td>{b._2}</td></tr>}
            }
          </table>))
    }
  }
}
