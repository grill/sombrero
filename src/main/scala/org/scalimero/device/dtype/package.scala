package org.scalimero.device.dtype {
  trait implicits extends num2ByteFloat.implicits with dateTime.implicits
    with num2ByteUnsigned.implicits with num4ByteUnsigned.implicits
    with num8BitUnsigned.implicits with string.implicits
}

package org.scalimero.device {
  package object dtype extends dtype.implicits
}
