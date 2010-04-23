package org.sombrero.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

//Superclass for all widget information dependant on the actual widget class, see org.sombrero.util.WidgetList.
//Foreign key needs alias in WidgetMetaData due to type system strangeness. (and the fact that you are not meant to do Mapper superclasses...)
trait WidgetData[T <: WidgetData[T]] extends LongKeyedMapper[T] {
self:T =>   
  object widget extends MappedLongForeignKey(this, Widget) {
    override def dbIndexed_? = true
    override def _toForm = Empty
  }
};
trait WidgetMetaData[T <: WidgetData[T]] extends LongKeyedMetaMapper[T] {
self:T =>
  def _widget = widget
};