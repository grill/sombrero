//author: Alexander C. Steiner
package org.sombrero.model
import net.liftweb.mapper._
import net.liftweb.util._
import _root_.net.liftweb.http.RequestVar

//Saves Room data, including image.
//Null parent foreign key means this is a root room.
class Room extends LongKeyedMapper[Room] with IdPK /*with LifecycleCallbacks*/ {
  def getSingleton = Room
   
  object name extends MappedString(this, 25) {
    override def dbIndexed_? = true
  }
   
  object parent extends MappedLongForeignKey(this, Room) {
    override def dbIndexed_? = true
  } 
  
  object image extends MappedBinary(this)
  object imageMime extends MappedString(this,100)
  
  def widgets = Widget.findAll(By(Widget.room, this.id))
  def children = Room.findAll(By(Room.parent, this.id))
  
  override def delete_! = { Widget.bulkDelete_!! (By(Widget.room, this.id)); super.delete_! }
}  
 
object Room extends Room with LongKeyedMetaMapper[Room] {
  def roots = Room.findAll(By(Room.parent, Empty))
  object currentVar extends RequestVar[Box[Room]](Empty)
  def current = currentVar.is
}
