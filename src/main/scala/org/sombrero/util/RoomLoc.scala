package org.sombrero.util
 
import _root_.net.liftweb.util._
import org.sombrero.snippet._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._
import _root_.scala.xml._
import org.sombrero.util._;
import org.sombrero.widget._;
import org.sombrero.widget.knx._;

import org.sombrero.model.Room
import org.sombrero.model.User
import org.sombrero.comet.CometWidget


abstract class RoomAccess 
case object NoSuchRoom extends RoomAccess
case class FullRoomAccess(room : Room) extends RoomAccess

class RoomLoc extends Loc[RoomAccess] {

  def response(path : List[String]) = new RewriteResponse(ParsePath(path, "", true, false), Map.empty, true)

  override def rewrite = Full({
    case RewriteRequest(ParsePath(List("room", aid), _, _, _), _, _) => {
      Log.info("rewrite called")
      try {
        val rid = aid.toLong
        Room.findAll(By(Room.id, rid)) match {
          case List(room) => {
            Room.currentVar(Full(room))
            (response("room" :: "display" :: Nil),
            FullRoomAccess(room))
          }
          case _ => {
            (response("room" :: "display" :: Nil),
            NoSuchRoom)
          }
        }
       } catch { case e : NumberFormatException => (RewriteResponse("room" :: Nil),
            NoSuchRoom) }
    }
    /*
    case RewriteRequest(ParsePath(List("room", aid, "widgetadd"), _, _, _), _, _) => {
      Log.info("rewrite called")
      try {
        val rid = aid.toLong
        Room.findAll(By(Room.id, rid)) match {
          case List(room) => {
            (response("room" :: "widgetadd" :: Nil),
            FullRoomAccess(room))
          }
          case _ => {
            (response("room" :: "display" :: Nil),
            NoSuchRoom)
          }
        }
       } catch { case e : NumberFormatException => (response("room" :: "display" :: Nil),
            NoSuchRoom) }
    }
    */
  })
      
  def roomRender(room : Room)(ignore : NodeSeq) : NodeSeq = {
    //var l : List[widget.Widget] = room.widgets.map((w : model.Widget) => w match {
      //case w if(WidgetList.map.contains(w.wclass.is)) => WidgetList.map(w.wclass.is).factory(w)
      /*case w =>*/ CometWidget.render(room)
      JavaScriptHelper.onLoad("""
    		$("#col3_content").attr("style", "background: white url(/room/""" + room.id.is + """/image) no-repeat top right");
      """)
      //case _ => null
    //})//.filter(_ != null)
    //l.foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq
  }
   
  override def snippets = {
    case ("roomview", Full(FullRoomAccess(room))) if User.loggedIn_? => roomRender(room)
    case ("roomview", _) => { ignore : NodeSeq => Text("Room not found.") }
  }
  
  override def defaultParams = Empty
  override def params = Nil
  override def link = new Link("room" :: Nil, true)
  override def name = "Rooms"
  override def text = "Rooms"

}
