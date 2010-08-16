package org.scalimero.dsl

import tuwien.auto.calimero.link._
import tuwien.auto.calimero.link.medium._
import tuwien.auto.calimero.process._
import tuwien.auto.calimero._
import tuwien.auto.calimero.datapoint._
import scala.actors.Actor
import scala.actors.Actor._

object Network {
  var default : Network = null
  var defaultMedium = TPSettings.TP1
  
  def apply(router : String, medium : KNXMediumSettings = defaultMedium) = {
    default = new Network(router, medium)
    default
  }
  
  def open = default.open
  def close = default.close
}

case class WriteEvent(value : String, destination : GroupAddress)

class Network(var router : String, var medium : KNXMediumSettings = Network.defaultMedium) {
  var nl : KNXNetworkLink = null
  var opened = false
  var pc : ProcessCommunicator = null
  
  val act = actor {
    var subscriptions = Map[GroupAddress,List[Actor]]() withDefaultValue Nil
    loop {
      react{
        case Subscribe(a, gas) => gas foreach {ga => subscriptions = subscriptions updated (ga, a :: subscriptions(ga))}
        case Unsubscribe(a) => subscriptions filterNot {_._2 == a}
        case e : DetachEvent => subscriptions flatMap {_._2} foreach {_ ! e}
        case e : ProcessEvent => subscriptions(e.getDestination) foreach {_ ! e}
        case e : WriteEvent => subscriptions(e.destination) foreach {_ ! e}
      }
    }
  }
  
  case class Subscribe(a : Actor, ga : List[GroupAddress])
  case class Unsubscribe(a : Actor)
  object pl extends ProcessListener{
    def detached(e : DetachEvent) {act ! e}
    def groupWrite(e : ProcessEvent) {act ! e}
  }
  
  def subscribe(a : Actor, ga : List[GroupAddress]) = act ! Subscribe(a : Actor, ga : List[GroupAddress])
  def unsubscribe(a : Actor) = act ! Unsubscribe(a : Actor)
  
  def open {
    pc = new ProcessCommunicatorImpl(networkLink)
    pc.addProcessListener(pl)
    opened = true
  }
  
  def close {
    opened = false
    if(nl != null && nl.isOpen)
      nl.close
  }
  
  def send(dp : Datapoint, value : String) = {
    act ! WriteEvent(value, dp.getMainAddress)
    pc.write(dp, value)
  }
  def read(dp : Datapoint) = pc.read(dp)
  
  def networkLink = if(nl != null && nl.isOpen) nl else {
    new KNXNetworkLinkIP(router, medium)
  }
  
  def apply(stuff : =>Unit) {
    val olddefault = Network.default
    Network.default = this
    stuff
    Network.default = olddefault
  }
}
