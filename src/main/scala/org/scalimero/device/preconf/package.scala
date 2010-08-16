package org.scalimero.device

import org.scalimero.device.dtype._

package object preconf {
  implicit def true2bool(t: True.type) = true
  implicit def false2bool(t: False.type) = false
  implicit def true2bool(t: True) = true
  implicit def false2bool(t: False) = false
  
  implicit def bool2True_False(t: Boolean) = if(t) True else False
}