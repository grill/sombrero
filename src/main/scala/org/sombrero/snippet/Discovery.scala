package org.sombrero.snippet

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

class Discovery {
  def discover() : JsCmd = {
    val d = new Discoverer(0, false)
    
    d.startSearch(10, true)
    
    val responses = d.getSearchResponses;
    
    Log.info("hallo");
    Log.info(responses)
    Log.info(responses.length.toString)
    
    SetHtml("routerlist",
    <table id="routerlist">
      {responses.foldLeft(Text("") : NodeSeq)
        {(a,b) => a ++ <tr><td>{Log.info(b); Log.info(b.getControlEndpoint);b.getControlEndpoint.toString}</td></tr>}
      }
    </table>
    )
  }

  def render( xhtml : NodeSeq ) : NodeSeq = {
    bind("discover", xhtml,
      "button" -> ajaxButton("start router discovery", discover _) )
  }
}
