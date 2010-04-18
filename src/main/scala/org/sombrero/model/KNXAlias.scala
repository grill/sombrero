package org.sombrero.model
import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.http.FieldError
import scala.xml.Text

//Aliases of KNX addresses (one device can be part of more than one group).
//m:n between KNXGroup and KNXWidget
class KNXAlias extends LongKeyedMapper[KNXAlias] with IdPK {
  def getSingleton = KNXAlias
  
  object alias extends MappedLongForeignKey(this, KNXGroup) {
    override def dbIndexed_? = true
  }
  
  object target extends MappedLongForeignKey(this, KNXWidget) {
    override def dbIndexed_? = true
  }
}
  
object KNXAlias extends KNXAlias with LongKeyedMetaMapper[KNXAlias] {
  def apply(theAlias : KNXGroup, theTarget : KNXWidget) : KNXAlias = {
    find(By(alias, theAlias), By(target, theTarget)) openOr
         create.alias(theAlias).target(theTarget).saveMe
  }
  
  def toggle(theAlias : KNXGroup, theTarget : KNXWidget) {
    KNXAlias.find(By(alias, theAlias), By(target, theTarget)) match {
      case Full(w) => w.delete_!
      case Empty => create.alias(theAlias).target(theTarget).save
    }
  }
}
