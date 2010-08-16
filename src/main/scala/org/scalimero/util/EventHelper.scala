package org.scalimero.util

import scala.concurrent.ops._

trait EventHelper[T] {
  class EventCallback(var fun : () => Unit) {
    def apply() = fun()
    def update(newFun : => Unit) = fun = newFun _
    def detach {eventUnsubscribe(this)}
  }

  val events : Map[Any, T => Boolean] = Map()
  var callbacks = Map[Any, List[EventCallback]]() withDefaultValue Nil
  
  def eventList(in : T) =
    events filter {_._2(in)} map {_._1}
  
  def eventSubscribe(event : Any)(callback : => Unit) = {
    if (events forall {_._1 != event})
      throw new NoSuchEventException("No event " + event)
    
    val ecallback = new EventCallback(callback _)
    callbacks = callbacks updated (event, ecallback :: callbacks(event))
    ecallback
  }
  
  def eventUnsubscribe(callback : EventCallback) = {
    callbacks = callbacks mapValues (_ filterNot (_==callback))
  }
  
  def callEvents(value : T){
    events.map((eventSig) => if(eventSig._2(value)) callbacks(eventSig._1).map(op => op()))
  }
}

class NoSuchEventException(msg : String) extends Exception(msg)
