package org.sombrero.view

import _root_.net.liftweb.util._
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

class WidgetLoc extends Loc[WidgetAccess] {

  def response(path : List[String]) = new RewriteResponse(ParsePath(path, "", true, false), Map.empty, true)

  override def rewrite = Full({
    //case RewriteRequest(ParsePath("widget" :: _, _, _, _), _, _) if (! User.superUser_?) => {Log.info("hello?"); Log.info(User.superUser_?.toString); (response("widget" :: Nil), WidgetPermissionDenied)}
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
   
  def widgetForm(w : Widget) = (ignore : NodeSeq) => {
    //var to : String = "/"
    //w.completeForm("Save Widget", (w, wd) => w.room.obj.map((r : model.Room) => to = "/room/" + r.id.is), to)
    
    w.completeForm("Save Widget", (w, wd) => null, "/closeframe")
  } 
   
  override def snippets = {
    case ("widgetedit", _) if ! User.superUser_? => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetedit", Full(FullWidgetAccess(w))) => widgetForm(w)
    case ("widgetedit", Full(WidgetPermissionDenied)) => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetedit", _) => { ignore : NodeSeq => Text("Widget not found.") }
  }
     
  override def defaultParams = Empty
  override def params = Nil
  override def link = new Link("widget" :: Nil, true)
  override def name = "Widgets"
  override def text = "Widgets"
  
}

