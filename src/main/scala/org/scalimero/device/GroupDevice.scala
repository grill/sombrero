package org.scalimero.device

import org.scalimero.device.dtype._
import org.scalimero.dsl._
import org.scalimero.util._
import org.scalimero.device._

import scala.collection.mutable

import tuwien.auto.calimero.GroupAddress

class GroupDevice[DataPointValueType <: DPValue[PrimitiveType], PrimitiveType](dpt: DPType[DataPointValueType, PrimitiveType])
  extends mutable.HashSet[Device[DataPointValueType, PrimitiveType]] with TCommandDevice[DataPointValueType, PrimitiveType]{
  var proxyFun = (value: PrimitiveType) => value
  var master: StateDevice[DataPointValueType, PrimitiveType] = null

  def this(destAddress: GroupAddress, tt: TranslatorType, dpt: DPType[DataPointValueType, PrimitiveType], name: String = "", net: Network = Network.default){
    this(dpt)
    master = new StateDevice(destAddress, tt, dpt, name, net)
    master writeSubscribe { (value: PrimitiveType) =>
      val pvalue = proxyFun(value)
      this map {_ write pvalue} 
    }
  }

  override def send(value: DataPointValueType) = {
    val pvalue = proxyFun(value.value)
    this map {_ write pvalue}
  }

  //This Function is called right before forwarding to all
  //registered Devices starts
  def addProxyFunction(fun: (PrimitiveType) => PrimitiveType) = proxyFun = fun
}

class MultipleAddressDevice[DataPointValueType <: DPValue[PrimitiveType], PrimitiveType]
  (r: TStateDevice[PrimitiveType], w: TCommandDevice[DataPointValueType, PrimitiveType]) 
  extends TStateDevice[PrimitiveType] with TCommandDevice[DataPointValueType, PrimitiveType]{

  def this(raddr: GroupAddress, waddr: GroupAddress, tt: TranslatorType, dpt: DPType[DataPointValueType, PrimitiveType],
    name: String = "", net: Network = Network.default){
    this(new StateDevice(raddr, tt, dpt, name, net), new CommandDevice(waddr, tt, dpt, name, net))
  }

  override def send(value: DataPointValueType) = w send value
  override def read(): PrimitiveType = r.read
}