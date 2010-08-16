package org.scalimero.device.preconf

import org.scalimero.device._
import org.scalimero.device.dtype._
import org.scalimero.device.dtype.boolean._
import org.scalimero.device.dtype.translatortype._

import tuwien.auto.calimero.GroupAddress

object Switch {
  def apply(address : GroupAddress) = new Switch(address)
}

class Switch(address: GroupAddress) extends StateDevice(address, BOOLEAN, TRIGGER){
  def turn(value: BooleanValue) = send(value)

  override val events : Map[Any, Boolean => Boolean] = Map ()
}
