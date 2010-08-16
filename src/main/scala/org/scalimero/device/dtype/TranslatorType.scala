package org.scalimero.device.dtype

abstract class TranslatorType{
  val mainNumber: Int
}

class TranslatorTypeNotFoundException(msg: String) extends Exception("The value " + msg + "is not a vaild mainNumber!")

package object translatortype{
  implicit def Int2TT(mainNumber: Int) = mainNumber match {
    case BOOLEAN.mainNumber => BOOLEAN
    case STRING.mainNumber => STRING
    case DATE_TIME.mainNumber => DATE_TIME
    case DATE.mainNumber => DATE
    case CHARACTER_SET.mainNumber => CHARACTER_SET
    case ACCESS.mainNumber => ACCESS
    case NUM8BIT_UNSIGNED.mainNumber => NUM8BIT_UNSIGNED
    case NUM8BIT_SIGNED.mainNumber => NUM8BIT_SIGNED
    case TIME.mainNumber => TIME
    case NUM4OCTET_UNSIGNED.mainNumber => NUM4OCTET_UNSIGNED
    case NUM4OCTET_SIGNED.mainNumber => NUM4OCTET_SIGNED
    case NUM4OCTET_FLOAT.mainNumber => NUM4OCTET_FLOAT
    case NUM3BIT_CONTROLLED.mainNumber => NUM3BIT_CONTROLLED
    case NUM2OCTET_UNSIGNED.mainNumber => NUM2OCTET_UNSIGNED
    case NUM2OCTET_SIGNED.mainNumber => NUM2OCTET_SIGNED
    case NUM2OCTET_FLOAT.mainNumber => NUM2OCTET_FLOAT
    case NUM1BIT_CONTROLLED.mainNumber => NUM1BIT_CONTROLLED
    case _ => throw new TranslatorTypeNotFoundException(mainNumber.toString); NOTHING
  }

  object NOTHING extends TranslatorType{
    override val mainNumber = -1
  }
  
  object BOOLEAN extends TranslatorType{
    override val mainNumber = 1
  }

  object STRING extends TranslatorType{
    override val mainNumber = 16
  }
  
  object DATE_TIME extends TranslatorType{
    override val mainNumber = 19
  }
  
  object DATE extends TranslatorType{
    override val mainNumber = 11
  }
  
  object CHARACTER_SET extends TranslatorType{
    override val mainNumber = 4
  }
  
  object ACCESS extends TranslatorType{
    override val mainNumber = 15
  }
  
  object NUM8BIT_UNSIGNED extends TranslatorType{
    override val mainNumber = 5
  }
  
  object NUM8BIT_SIGNED extends TranslatorType{
    override val mainNumber = 6
  }
  
  object TIME extends TranslatorType{
    override val mainNumber = 10
  }
  
  object NUM4OCTET_UNSIGNED extends TranslatorType{
    override val mainNumber = 12
  }
  
  object NUM4OCTET_SIGNED extends TranslatorType{
    override val mainNumber = 13
  }
  
  object NUM4OCTET_FLOAT extends TranslatorType{
    override val mainNumber = 14
  }
  
  object NUM3BIT_CONTROLLED extends TranslatorType{
    override val mainNumber = 3
  }
  
  object NUM2OCTET_UNSIGNED extends TranslatorType{
    override val mainNumber = 7
  }
  
  object NUM2OCTET_SIGNED extends TranslatorType{
    override val mainNumber = 8
  }
  
  object NUM2OCTET_FLOAT extends TranslatorType{
    override val mainNumber = 9
  }
  
  object NUM1BIT_CONTROLLED extends TranslatorType{
    override val mainNumber = 2
  }
}