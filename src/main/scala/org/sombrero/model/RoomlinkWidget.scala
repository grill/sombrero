package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._ 

//Data for widgets that refer to other rooms gets stored here.
class RoomlinkWidget extends WidgetData[RoomlinkWidget] with IdPK{
  def getSingleton = RoomlinkWidget 
  
  object image extends MappedBinary(this) {
    var error = false
     
    override def _toForm = {
      def callback(fh : FileParamHolder) {
        //Log.info(fh.toString)
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
      is match {
        case null => Full(Text("add: ") ++ SHtml.fileUpload(callback _))
        case _ => Full(Text("replace: ") ++ SHtml.fileUpload(callback _))
      }
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
      //val setRoom : Room => RoomlinkWidget = this.apply _
      //Log.info(obj.toString);
      Full(SHtml.selectObj[Room](Room.findAll.map((room) => {
      /*Log.info(room.toString); Log.info(obj.equals(room).toString);*/ (room, room.name.is)}), obj, this(_))) //(r : Room) => {Log.info(r.toString); setRoom(r)}))
    }
  }
}

object RoomlinkWidget extends RoomlinkWidget with WidgetMetaData[RoomlinkWidget] {}
