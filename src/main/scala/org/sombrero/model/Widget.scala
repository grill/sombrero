package org.sombrero.model
import net.liftweb.mapper._
import net.liftweb.util.Empty

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.net.liftweb.http.js.JsCmds.SetHtml
import _root_.net.liftweb.http.js.JsCmds.Noop
import _root_.scala.xml._
import org.sombrero.comet._

import org.sombrero.util._
import WidgetList.WidgetClass

//Stores widget data that is common to all widgets.
class Widget extends LongKeyedMapper[Widget] with IdPK /*with LifecycleCallbacks*/ {
  def getSingleton = Widget
  
  object name extends MappedString(this, 32) {
   override def apply(s: String) = {
     Distributor ! TitleMessage(id.is, s)
     super.apply(s)
   }
   
   def notEmpty(in : String) : List[FieldError] = in match {
     case "" => List(FieldError(this, Text("Widget needs a name!")))
     case _ => List[FieldError]()
   }
   
   override def validations = notEmpty _ :: Nil
  }
  
  def pos : Position = Position(User.currentUser open_!, this)
  
  class ProxyField (parent : Widget, field : MappedInt[Position]) {
    def is = field.is
    def apply(i : Int) = {
      field(i).save
      parent
    }    
  }
  
  object left   extends ProxyField (this, pos.left)
  object top    extends ProxyField (this, pos.top)
  object width  extends ProxyField (this, pos.width)
  object height extends ProxyField (this, pos.height)
  
  
  //def defaultDataToForm (b : Box[String], n2n : (NodeSeq) => NodeSeq, a2u : (Any) => Unit) : NodeSeq = Text("")
  var dataToForm : ((Box[String], (NodeSeq) => NodeSeq, (Any) => Unit) => NodeSeq) => NodeSeq = null
    
  object wclass extends MappedString(this, 32) {
    private var realfilter : Box[WidgetMetaData[_]] = Empty
    def filter_=(newFilter : Box[WidgetMetaData[_]]) = {realfilter = newFilter; this}
    override def defaultValue = realfilter.map((f) => WidgetList.map.filter(_._2.data == f).values.next.id) openOr WidgetList.default.id
    
    override def _toForm = {
      def callback(newVal : String) = {
        val oldVal = is
        this(newVal)
        if(dataToForm != null)
        {
          val oldtype = WidgetList.map(oldVal).data
          val newtype = WidgetList.map(newVal).data
          if(oldtype != newtype)
            SetHtml("widgetdata", dataToForm(newtype.create.toForm _))
          else
            Noop
        }
        else
          Noop
      }
    
      if (saved_?) Empty else
      Full(SHtml.ajaxSelect(
          WidgetList.map.filter(
             realfilter.map((f:(WidgetMetaData[_])) => (wct:(String, WidgetClass[_])) => wct._2.data == f).openOr((_) => true))
          .map((wct) => (wct._2.name, wct._2.id)).toSeq, Full(is), callback _))
    }
  }
  
  def dataForm (redoSnippet : (NodeSeq) => NodeSeq, onSuccess : (WidgetData[_]) => Unit) : NodeSeq = {
    def onSubmit(something : Any) {
      something match {
        case wd : WidgetData[_] =>
          wd.widget(this)
          onSuccess(wd)
        case _ => ;
      }
    }
    
    dataToForm = (fu : (Box[String], (NodeSeq) => NodeSeq, (Any) => Unit) => NodeSeq) => fu(Empty, redoSnippet, onSubmit)
  
    if (! saved_?) <div id="widgetdata" >{dataToForm(WidgetList.map(wclass.is).data.create.toForm _)}</div> else
    <div id="widgetdata" >{data.map(_.toForm(Empty, redoSnippet, onSubmit)) openOr Text("")}</div>
  }
  
  def aliasForm () : NodeSeq =
    knx_? match {
      case Full(w) =>
        <table>
          {KNXGroup.findAll().flatMap(g =>
            <tr>
              <td> {
                checkbox(KNXAlias.exists_?(g, w), KNXAlias.set(g, w, _))
              } </td>
              <td> {
                Text(g.name.is)
              } </td>
            </tr>
          )}
        </table>
      case _ => Text("")
    }
  
  object room   extends MappedLongForeignKey(this, Room) {
    override def dbIndexed_? = true
    override def _toForm = Empty
  }
  
  def knx_? () : Box[KNXWidget] =
    KNXWidget.find(By(KNXWidget.widget, id.is))
    
  def knx () : KNXWidget = { 
    val knxlst = KNXWidget.findAll(By(KNXWidget.widget, id.is))
    if (knxlst.isEmpty)
      KNXWidget.create.widget(id.is).saveMe
    else
      knxlst.head
  }
  
  def roomlink () : RoomlinkWidget = {
    val lst = RoomlinkWidget.findAll(By(RoomlinkWidget.widget, id.is))
    if (lst.isEmpty)
      RoomlinkWidget.create.widget(id.is).saveMe
    else
      lst.head
  }
  
  def data () : Box[WidgetData[_]] = {
    WidgetList.map(wclass.is).data.find(By(WidgetList.map(wclass.is).data._widget, id.is))
  }
    
  def childs () : List[Widget] = {
    ContainerWidget.findAll(By(ContainerWidget.widget, id.is)).map(_.content.obj.open_!)
  }
  
  def addChild (w : model.Widget) : Widget = {
    ContainerWidget.create.widget(this).content(w).save
    this
  }    
  
  def removeChild (w: model.Widget) : Widget = {
    ContainerWidget.find(By(ContainerWidget.widget, id.is), By(ContainerWidget.content, w.id.is)).open_!.delete_!
    this
  }
  
  override def delete_! = {
    val knxlst = KNXWidget.findAll(By(KNXWidget.id, id.is))
    if (! knxlst.isEmpty)
      knxlst.head.delete_!
    super.delete_!
  }
}
  
object Widget extends Widget with LongKeyedMetaMapper[Widget] {
  def roomless = findAll(By(room, Empty))
}