//author: Alexander C. Steiner
package org.sombrero.snippet

import org.sombrero.model
import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import _root_.scala.xml._
import _root_.net.liftweb.mapper._

//various room functions
class Room {
  
  object redoSnippet extends RequestVar[Box[(NodeSeq) => NodeSeq]](Empty) 

  //add room form
  def add( xhtml : NodeSeq ) : NodeSeq = 
    redoSnippet.is.map(_(xhtml)) openOr {
    var newname = ""
    var parent = model.Room.current
    var fileHolder : Box[FileParamHolder] = Empty
    
    def realrender(xhtml : NodeSeq) : NodeSeq = {
    def newRoom() {
      if(newname != "") {
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
      } else {
        S.error("Room needs a name!"); redoSnippet(Full(realrender _))
      }
    }
    
    bind("room", xhtml,
      "name" -> text(newname, newname = _),
      "image" -> fileUpload((f : FileParamHolder) => fileHolder = Full(f)),
      "submit" -> submit("Create Room", newRoom _) )
    }
    realrender(xhtml)
  }
  
  //modify room form
  def modify( xhtml : NodeSeq ) : NodeSeq = 
    redoSnippet.is.map(_(xhtml)) openOr {
    model.Room.current.map{room =>
    var newname = room.name.is
    var fileHolder : Box[FileParamHolder] = Empty
    
    def realrender(xhtml : NodeSeq) : NodeSeq = {
      def newRoom() {
        if(newname != "") {
        room.name(newname)
      
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
        } else {
          S.error("Room needs a name!"); redoSnippet(Full(realrender _))
        }
      }
      
      bind("room", xhtml,
        "name" -> text(newname, newname = _),
        "imagelabel" -> (if(room.image.is == null) 
          Text("add")  else 
          Text("replace")),
        "image" -> (fileUpload((f : FileParamHolder) => fileHolder = Full(f))),
        "submit" -> submit("Modify Room", newRoom _) )
    }
    realrender(xhtml)
    } openOr Text("")
  }
  
  //delete room button
  def remove( xhtml : NodeSeq ) : NodeSeq = {
    model.Room.current.map(
      (room) =>
      {
        if (room.children != Nil)
          Text("Room cannot be deleted because it has children")
        else
          bind("room", xhtml, "current" -> submit("Delete Room", () => {room.delete_!; S redirectTo "/"}))
      }
      ) openOr Text("No Room to delete")
  }
  
  //room deletion list
  def removeList( xhtml : NodeSeq ) : NodeSeq = {
    model.Room.findAll.flatMap((room) =>
                      bind("room", xhtml,
                        "list" -> submit(room.name.is, room.delete_! _)))
  }  
  
  def name( xhtml : NodeSeq ) : NodeSeq = {
    Text(model.Room.current.map(_.name.is) openOr "")
  }
}
