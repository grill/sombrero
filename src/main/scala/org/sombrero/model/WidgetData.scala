package org.sombrero.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

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
//trait WidgetData[A] extends LongKeyedMapper[A];
//trait WidgetMetaData[A] extends LongKeyedMetaMapper[A];
       