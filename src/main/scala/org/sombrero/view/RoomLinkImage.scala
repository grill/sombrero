//author: Alexander C. Steiner
package org.sombrero.view

import org.sombrero.model.RoomlinkWidget
import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import _root_.scala.xml._
import _root_.net.liftweb.mapper._
  
//displays roomlink images
//throws exception when the roomlink doesn't exist, so be careful
object RoomLinkImage {
  def get(rlid : String) : Box[LiftResponse] = {
    try {
      RoomlinkWidget.findAll(By(RoomlinkWidget.id, Integer.parseInt(rlid))) match {
        case List(rl) => {
          Full(InMemoryResponse(rl.image.is,
              ("Content-Type" -> rl.imageMime.is) :: Nil,
              Nil,
              200))
        }
        case _ => Empty
      }
    } catch { case e : NumberFormatException => Empty }
  }
}
 
