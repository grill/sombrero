//author: Alexander C. Steiner
package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.model
import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import java.net._;
import _root_.net.liftweb.mapper._

//builds a structure of links to represent the rooms
class RoomList { 
  def render(xhtml : NodeSeq) : NodeSeq = {
    recursiveRender(xhtml, Room.roots)
  }
  
  def recursiveRender(xhtml : NodeSeq, roomlist : List[model.Room]) : NodeSeq = {
    roomlist.flatMap((room) =>
      bind("room", xhtml,
        "link" -> {
          <a href={"/room/"+room.id.is}>{Text(room.name.is)}</a>.map(el => 
            el match{ case e: Elem => e % new UnprefixedAttribute("class", room.id.is.toString, Null) })},
        "children" -> recursiveRender(xhtml, room.children)))
  }
} 
  
