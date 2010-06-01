//author: Alexander C. Steiner
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
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse
import scala.concurrent.ops._

//router discovery
class Discovery extends CometActor {
  override def defaultPrefix = Full("rtr")
  
  def render = bind("now" -> Text(model.KNXRouter.get_?.map(_.ip.is) openOr "Nothing!"),
      "entries" -> <table id="routerlist" />,
      "button" ->  ajaxButton("Start discovery", () => {spawn{discover()}; Noop}))
      
  def discover() = {
    val d = new Discoverer(0, false)
    
    d.startSearch(10, true)
    
    val resp : Array[SearchResponse] = d.getSearchResponses
    
    Log.info(model.KNXRouter.get.ip.is)
    Log.info(resp.toString)
    Log.info(resp.length.toString)
    //Log.info(resp(1).toString)
    resp.foreach(r => Log.info(r.getControlEndpoint.toString))
    
    this ! resp
  }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
  case cmd : JsCmd => {
      partialUpdate(cmd)
    }
  case resp : Array[SearchResponse] => {
    this ! SetHtml("routerlist",
    <table id="routerlist">
      {resp.flatMap{b =>
          <tr>
            <td>
              {SHtml.link("/discovery", {() => model.KNXRouter.get.ip(b.getControlEndpoint.getAddress.getHostAddress.toString).save}, Text(b.getControlEndpoint.toString))}
            </td>
          </tr>}
      }
    </table>
    )
    }
  case _ => Log.info("OH NOEZ");
  }
}
