//author: Alexander C. Steiner
package org.sombrero.model
import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.http.FieldError
import scala.xml.Text


//KNX group that (usually) contains multiple widgets.
class KNXGroup extends LongKeyedMapper[KNXGroup] with IdPK {
  def getSingleton = KNXGroup
  
  object name extends MappedString(this, 20) {
    override def dbIndexed_? = true
    
   def notEmpty(in : String) : List[FieldError] = in match {
     case "" => List(FieldError(this, Text("KNX group needs a name!")))
     case _ => List[FieldError]()
   }
   
    override def validations = notEmpty _ :: Nil
  }
  
  object address extends MappedString(this, 15) {
    override def dbIndexed_? = true
    
    def correctAddress(in : String) =
      if(KNXGroup.correctAddress(in)) Nil
      else List(FieldError(this, Text("incorrect KNX address format")))
    
    override def validations = correctAddress _ :: Nil
  }
  
  def getWidgets =
    KNXAlias.findAll(By(KNXAlias.alias, this)).map(_.target.obj.open_!)
}
  
object KNXGroup extends KNXGroup with LongKeyedMetaMapper[KNXGroup] {
  def correctAddress(in : String) =
    Matchers.knx.findPrefixMatchOf(in) != None
  
  def getWidgets(theAddress : String) : List[KNXWidget] =
    KNXGroup.find(By(address, theAddress)).map(_.getWidgets) openOr KNXWidget.findAll(By(KNXWidget.groupAddress, theAddress)) 
}
