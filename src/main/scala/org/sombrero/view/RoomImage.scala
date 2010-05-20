package org.sombrero.view

import org.sombrero.model.Room
import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import _root_.scala.xml._
import _root_.net.liftweb.mapper._
  
object RoomImage {
  def get(roomid : String) : Box[LiftResponse] = {
    try {
      Room.findAll(By(Room.id, Integer.parseInt(roomid))) match {
        case List(room) => {
          Full(InMemoryResponse(room.image.is,
              ("Content-Type" -> room.imageMime.is) :: Nil,
              Nil,
              200))
        }
        case _ => Empty
      }
    } catch { case e : NumberFormatException => Empty }
  }
}
 
