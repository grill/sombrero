package org.scalimero.device.preconf

import org.scalimero.device._
import org.scalimero.device.dtype._
import org.scalimero.device.dtype.boolean._
import org.scalimero.device.dtype.translatortype._

import tuwien.auto.calimero.GroupAddress

object Lamp {
  def apply(address : GroupAddress) = new Lamp(address)
}

class Lamp(address: GroupAddress) extends StateDevice(address, BOOLEAN, SWITCH){
  def turn(value: BooleanValue) = send(value)

  override val events : Map[Any, Boolean => Boolean] = Map (
    on ->   {(b: Boolean) => b},
    off ->  {(b: Boolean) => !b}
  )
}
