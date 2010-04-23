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
  
class Room {
  def add( xhtml : NodeSeq ) : NodeSeq = {
    var newname = ""
    var parent = model.Room.current
    var fileHolder : Box[FileParamHolder] = Empty
    
    def newRoom() {
      val room = model.Room.create.name(newname).parent(parent)
    
      fileHolder match {
        case Full(FileParamHolder(_, null, _, _)) => true
        case Full(FileParamHolder(_, mime, _, data))
          if mime.startsWith("image/") => {
            room.image(data).imageMime(mime)
          true
        }
        case Full(_) => false
        case _ => true
        }

      room.save
    }
    
    bind("room", xhtml,
      "name" -> text(newname, newname = _),
      "image" -> fileUpload((f : FileParamHolder) => fileHolder = Full(f)),
      "submit" -> submit("Create Room", newRoom _) )
  }
  
  def remove( xhtml : NodeSeq ) : NodeSeq = {
    model.Room.current.map(
      (room) =>
      {
        if (room.children != Nil)
          Text("Room has children")
        else
          bind("room", xhtml, "current" -> submit("Delete Room", () => {room.delete_!; S redirectTo "/"}))
      }
      ) openOr Text("No Room to delete")
  }
  
  def removeList( xhtml : NodeSeq ) : NodeSeq = {
    model.Room.findAll.flatMap((room) =>
                      bind("room", xhtml,
                        "list" -> submit(room.name.is, room.delete_! _)))
  }  
  
  def name( xhtml : NodeSeq ) : NodeSeq = {
    Text(model.Room.current.map(_.name.is) openOr "no Room")
  }
}