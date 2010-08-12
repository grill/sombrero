//author: Alexander C. Steiner
package org.sombrero.model
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.util.Helpers._   

//Every User can have an arbitrary number of favorite Widgets.
//This is essentially an m:n relation.
class Fav extends LongKeyedMapper[Fav] with IdPK{
  def getSingleton = Fav
  
  object user extends MappedLongForeignKey(this, User) {
    override def dbIndexed_? = true
  }
  object widget extends MappedLongForeignKey(this, Widget) {
    override def dbIndexed_? = true
  } 
}  
     
object Fav extends Fav with LongKeyedMetaMapper[Fav] {
  val htmlid = "Fav"
  
  def get = findAll(By(user, User.currentUser)).map(_.widget.obj).filter(_ != Empty).map(_.open_!)
  def add(w : Widget) = create.user(User.currentUser).widget(w).save
  def remove(w : Widget) : Unit = findAll(By(user, User.currentUser), By(widget, w)).map(_.delete_!)
  def isFav(w: Widget) = findAll(By(Fav.widget, w), By(user, User.currentUser)) != Nil
}
