//author: Alexander C. Steiner
package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._

//This saves KNX relevant data for a widget.
class KNXRouter extends LongKeyedMapper[KNXRouter] with IdPK{
  def getSingleton = KNXRouter

  object ip extends MappedString(this, 15) {
    def correctAddress(in : String) : List[FieldError] = {
      if(Matchers.ip.findPrefixMatchOf(in) == None) {
        List(FieldError(this, Text("incorrect IP address format")))
      } else {
        List[FieldError]()
      }
    }
    override def validations = correctAddress _ :: Nil
  }  
}  

object KNXRouter extends KNXRouter with LongKeyedMetaMapper[KNXRouter] {
  def get = get_? openOr create
  
  def get_? = KNXRouter.findAll match {
    case List(knxr) => Full(knxr)
    case _ => Empty
  }
  
  def getIP = get_?.map(_.ip.is)
}
