//author: Alexander C. Steiner
package org.sombrero.view

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import org.sombrero.snippet._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import SHtml._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._
import _root_.scala.xml._
import org.sombrero.util._;
//import org.sombrero.widget._;
//import org.sombrero.widget.knx._;

import org.sombrero.model._
 

abstract class WidgetAccess 
case object WidgetNotFound extends WidgetAccess
case object WidgetPermissionDenied extends WidgetAccess
case class FullWidgetAccess(w : Widget) extends WidgetAccess

//handles widget edit forms
class WidgetLoc extends Loc[WidgetAccess] {

  def response(path : List[String]) = new RewriteResponse(ParsePath(path, "", true, false), Map.empty, true)

  override def rewrite = Full({
    case RewriteRequest(ParsePath(List("widget", aid), _, _, _), _, _) => {
      Log.info("rewrite!")
      try {
        val wid = aid.toLong
        Widget.findAll(By(Widget.id, wid)) match {
          case List(w) => {
            (response("widget" :: Nil),
            FullWidgetAccess(w))
          } 
          case _ => {
            (response("widget" :: Nil),
            WidgetNotFound)
          }
        }
       } catch { case e : NumberFormatException => (RewriteResponse("widget" :: Nil),
            WidgetNotFound) }
    }
  })
   
  //the snippet
  def widgetForm(w : Widget) = (ignore : NodeSeq) => {
    w.completeForm("Save Widget", (w, wd) => null, "/closeframe")
  } 
   
  override def snippets = {
    case ("widgetedit", _) if ! User.superUser_? => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetedit", Full(FullWidgetAccess(w))) => widgetForm(w)
    case ("widgetedit", Full(WidgetPermissionDenied)) => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetedit", _) => { ignore : NodeSeq => Text("Widget not found.") }
  }
     
  override def defaultValue = Full(WidgetNotFound)
  override def params = Nil
  override def link = new Link("widget" :: Nil, true)
  override def name = "Widgets"
  override def text = "Widgets"
  
}

