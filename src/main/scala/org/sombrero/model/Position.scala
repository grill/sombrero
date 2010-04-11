package org.sombrero.model
import net.liftweb.mapper._
import net.liftweb.util.Empty
import net.liftweb.util.Log
import _root_.net.liftweb.http.RequestVar

class Position extends LongKeyedMapper[Position] with IdPK {
  def getSingleton = Position
  
  object left   extends MappedInt(this) {
    override def defaultValue = 0
  }
  object top    extends MappedInt(this) {
    override def defaultValue = 0
  }
  object width  extends MappedInt(this) {
    override def defaultValue = 160
  }
  object height extends MappedInt(this) {
    override def defaultValue = 160
  }   
  
  object widget extends MappedLongForeignKey(this, Widget) {
    override def dbIndexed_? = true
  }     
  object user   extends MappedLongForeignKey(this, User) {
    override def dbIndexed_? = true
  }    
}
  
object Position extends Position with LongKeyedMetaMapper[Position] {
  def apply(u : User, w : Widget) : Position = {
    find(By (this.user, u.id.is), By (this.widget, w.id.is)) openOr create.user(u).widget(w).saveMe
  }
  object leftVar extends RequestVar[Int](0)
  val maxLeft = 640
  def nextLeft = {val oldLeft = leftVar.is; leftVar(leftVar.is + width.defaultValue); oldLeft}
  override def create = {
    val pos = nextLeft
    Log.info("pos: " + pos)
    super.create.left(pos % maxLeft).top(pos/maxLeft * height.defaultValue)
  }
}
