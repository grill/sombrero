package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
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

class RoomList { 
/*
  def render(xhtml : NodeSeq) : NodeSeq = {
    Room.findAll.flatMap((room) =>
      bind("room", xhtml, "link" -> link("/room/"+room.id.is, () => Empty , Text(room.name.is))))
  }
  */
  def render(xhtml : NodeSeq) : NodeSeq = {
    recursiveRender(xhtml, Room.roots)
  }
  
  def recursiveRender(xhtml : NodeSeq, roomlist : List[model.Room]) : NodeSeq = {
    roomlist.flatMap((room) =>
      bind("room", xhtml,
        "link" -> {/*if(model.Room.current.map(_.id.is == room.id.is) openOr false) {Text(room.name.is)} else {*/ 
          //link("/room/"+room.id.is, () => Empty , Text(room.name.is)).map(el => 
          <a href={"/room/"+room.id.is}>{Text(room.name.is)}</a>.map(el => 
            el match{ case e: Elem => e % new UnprefixedAttribute("class", room.id.is.toString, Null) })}/*}*/,
        "children" -> recursiveRender(xhtml, room.children)))
  }
} 
  
