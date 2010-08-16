package org.scalimero.device.preconf

import org.scalimero.device._
import org.scalimero.device.dtype._
import org.scalimero.device.dtype.num8BitUnsigned._
import org.scalimero.device.dtype.translatortype._

import tuwien.auto.calimero.GroupAddress

object Dimmer {
  def apply(address : GroupAddress) = new Dimmer(address)
}

class Dimmer(address: GroupAddress) extends StateDevice(address, NUM8BIT_UNSIGNED, SCALING){
  def set(value: SCALING) = send(value)

  override val events : Map[Any, Int => Boolean] = Map ()
}