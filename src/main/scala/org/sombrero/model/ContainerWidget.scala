package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.util.Helpers._   

class ContainerWidget extends LongKeyedMapper[ContainerWidget] with IdPK{
  def getSingleton = ContainerWidget
  
  object content extends MappedLongForeignKey(this, Widget)
  object widget extends MappedLongForeignKey(this, Widget) {
    override def dbIndexed_? = true
  }    
}         
 
object ContainerWidget extends ContainerWidget with LongKeyedMetaMapper[ContainerWidget] {}
    