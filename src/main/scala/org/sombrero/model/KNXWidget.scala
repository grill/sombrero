package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._

//This saves KNX relevant data for a widget.
class KNXWidget extends WidgetData[KNXWidget] with IdPK{
  def getSingleton = KNXWidget
   
  object groupAddress extends MappedString(this, 15) {//???
    def correctAddress(in : String) : List[FieldError] = {
      if(Matchers.knx.findPrefixMatchOf(in) == None) {
        List(FieldError(this, Text("incorrect KNX address format")))
      } else {
        List[FieldError]()
      }
    }
    override def validations = correctAddress _ :: Nil
  }
  object remoteHost extends MappedString(this, 15) {
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

object KNXWidget extends KNXWidget with WidgetMetaData[KNXWidget] {}
