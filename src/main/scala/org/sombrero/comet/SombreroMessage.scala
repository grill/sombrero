//author: Alexander C. Steiner
package org.sombrero.comet

//different messages for the various actors
abstract class SombreroMessage(val id : Long)

case class TestMessage(override val id : Long, val text : String) extends SombreroMessage(id)
case class TitleMessage(override val id : Long, val s: String) extends SombreroMessage(id)
case class DBMessage(override val id : Long) extends SombreroMessage(id)
case class KNXMessage(override val id : Long, val newVal : Array[Byte]) extends SombreroMessage(id)
case class KNXWriteMessage(override val id : Long, val newVal : String) extends SombreroMessage(id)
case class FavAddMessage(override val id : Long) extends SombreroMessage(id)
case class FavRemMessage(override val id : Long) extends SombreroMessage(id)
