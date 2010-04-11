package org.sombrero.comet
 
abstract case class SombreroMessage(id : Long)
//id2: ugly as hell hack
case class TestMessage(id2 : Long, text : String) extends SombreroMessage(id2)
case class TitleMessage(id2 : Long, s: String) extends SombreroMessage(id2) //maybe add some sort of parameter that allows the receiver to identify the changed field
case class DBMessage(id2 : Long) extends SombreroMessage(id2) //maybe add some sort of parameter that allows the receiver to identify the changed field
case class KNXMessage(id2 : Long, newVal : Array[Byte]) extends SombreroMessage(id2)
  