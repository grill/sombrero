package org.sombrero.comet

import org.sombrero.model
import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import _root_.scala.xml._
import _root_.net.liftweb.mapper._
import tuwien.auto.calimero.knxnetip.Discoverer
import scala.concurrent.ops._

class Discovery extends CometActor {
  override def defaultPrefix = Full("rtr")
  
  def render = bind("entries" -> <table id="routerlist" />,
      "button" ->  ajaxButton("Start discovery", () => {spawn{discover()}; Noop}))
      
  def discover() = {
    val d = new Discoverer(0, false)
    
    d.startSearch(10, true)
    
    Log.info(d.getSearchResponses.length.toString)
    
    this ! SetHtml("routerlist",
    <table id="routerlist">
      {d.getSearchResponses.foldLeft(Nil : NodeSeq)
        {(a,b) => a ++ <tr><td>{b.getControlEndpoint.toString}</td></tr>}
      }
    </table>
    )
  }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
  case cmd : JsCmd => {
      partialUpdate(cmd)
    }
  }
}
