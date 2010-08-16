package org.scalimero

import org.scalimero.device.dtype._

import tuwien.auto.calimero.GroupAddress

package object device {
  //Device type Boolean
  implicit def true2bool(t: True.type) = true
  implicit def false2bool(t: False.type) = false
  implicit def true2bool(t: True) = true
  implicit def false2bool(t: False) = false
  implicit def bool2True_False(t: Boolean) = if(t) True else False
  
  //KNX address
  implicit def str2groupaddr(s: String) = new GroupAddress(s) 
}