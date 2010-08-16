package org.scalimero

import org.scalimero.device.dtype._

package object dsl {
  implicit def true2bool(t: True.type) = true
  implicit def false2bool(t: False.type) = false
  implicit def true2bool(t: True) = true
  implicit def false2bool(t: False) = false
  implicit def bool2True_False(t: Boolean) = if(t) True else False
}