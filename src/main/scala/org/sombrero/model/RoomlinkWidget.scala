//author: Alexander C. Steiner
package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.scala.xml._ 

//Data for widgets that refer to other rooms gets stored here.
class RoomlinkWidget extends WidgetData[RoomlinkWidget] with IdPK{
  def getSingleton = RoomlinkWidget 
  
  object image extends MappedBinary(this) {
    var error = false
    
    override def displayName = 
      is match {
        case null => "add image"
        case _ => "replace image"
      }
     
    override def _toForm = {
      def callback(fh : FileParamHolder) {
        fh match {
        case FileParamHolder(_, _, "", _) => error = false
        case FileParamHolder(_, mime, _, data)
          if mime.startsWith("image/") => {
            this(data).imageMime(mime)
          error = false
        }
        case _ => {
          error = true
        }
      }
      }
      Full(SHtml.fileUpload(callback _))
    }
    
    def onlyImageMime(data : Array[Byte]) : List[FieldError] = {
      if(error) {
        //Log.info("Error thrown!")
        error = false
        List(FieldError(this, Text("Uploaded file must be an image.")))
      } else {
        //Log.info("valid!")
        List[FieldError]()
      }
    }
    
    override def validations = {(data : Array[Byte]) => onlyImageMime(data)} :: Nil
  }
  object imageMime extends MappedString(this,100) {
    override def _toForm = Empty
  }
  
  object room extends MappedLongForeignKey(this, Room) {
    override def _toForm = {
      Full(SHtml.selectObj[Room](Room.findAll.map((room) => {
      (room, room.name.is)}), obj, this(_)))
    }
  }
}

object RoomlinkWidget extends RoomlinkWidget with WidgetMetaData[RoomlinkWidget] {}
