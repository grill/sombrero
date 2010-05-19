package org.sombrero.comet
 
abstract class SombreroMessage(val id : Long)

case class TestMessage(override val id : Long, val text : String) extends SombreroMessage(id)
case class TitleMessage(override val id : Long, val s: String) extends SombreroMessage(id)
case class DBMessage(override val id : Long) extends SombreroMessage(id) //maybe add some sort of parameter that allows the receiver to identify the changed field
case class KNXMessage(override val id : Long, val newVal : Array[Byte]) extends SombreroMessage(id)
  
