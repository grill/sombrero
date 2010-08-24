//author: Alexander C. Steiner
package org.sombrero.view

import _root_.net.liftweb.util.Helpers
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
import org.sombrero.comet.CometWidget
 

abstract class WidgetViewAccess 
case object WidgetViewNotFound extends WidgetViewAccess
case object WidgetViewPermissionDenied extends WidgetViewAccess
case class FullWidgetViewAccess(w : Widget) extends WidgetViewAccess

//handles widget edit forms
class WidgetViewLoc extends Loc[WidgetAccess] {

  def response(path : List[String]) = new RewriteResponse(ParsePath(path, "", true, false), Map.empty, true)

  override def rewrite = Full({
    case RewriteRequest(ParsePath(List("widgetview", aid), _, _, _), _, _) => {
      Log.info("rewrite!")
      try {
        val wid = aid.toLong
        Widget.findAll(By(Widget.id, wid)) match {
          case List(w) => {
            (response("widgetview" :: Nil),
            FullWidgetAccess(w))
          } 
          case _ => {
            (response("widgetview" :: Nil),
            WidgetNotFound)
          }
        }
       } catch { case e : NumberFormatException => (RewriteResponse("widgetview" :: Nil),
            WidgetNotFound) }
    }
  })
   
  //the snippet
  def widgetView(w : Widget) = (ignore : NodeSeq) => {
    CometWidget.renderSingle(w.id.toString).toList :::
    WidgetList.map(w.wclass.is).singlewidget(w).render.toList : NodeSeq
  } 
   
  override def snippets = {
    case ("widgetview", _) if ! User.loggedIn_? => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetview", Full(FullWidgetAccess(w))) => widgetView(w)
    case ("widgetview", Full(WidgetPermissionDenied)) => { ignore : NodeSeq => Text("Permission denied.") }
    case ("widgetview", _) => { ignore : NodeSeq => Text("Widget not found.") }
  }
     
  override def defaultValue = Full(WidgetNotFound)
  override def params = Nil
  override def link = new Link("widgetview" :: Nil, true)
  override def name = "WidgetView"
  override def text = "Widget View"
  
}

