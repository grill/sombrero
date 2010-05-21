//author: Alexander C. Steiner
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
  
  var dataToForm : ((Box[String], (NodeSeq) => NodeSeq, (Any) => Unit) => NodeSeq) => NodeSeq = null
    
  object wclass extends MappedString(this, 32) {
    object filter extends RequestVar[Box[WidgetMetaData[_]]](Empty)
    override def defaultValue = WidgetList.default.id
    
    override def displayName = "type"
    
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
      
      if (saved_?) filter(data.map(_.getSingleton))
    
      Full(SHtml.ajaxSelect(
          WidgetList.map.filter(
             filter.is.map((f:(WidgetMetaData[_])) => (wct:(String, WidgetClass[_])) => wct._2.data == f).openOr((_) => true))
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
  
  def dataForm[Data <: WidgetData[Data]] (initData : Data, redoSnippet : (NodeSeq) => NodeSeq, onSuccess : (Data) => Unit) : NodeSeq = {
    def onSubmit(something : Any) {
      something match {
        case wd : Data =>
          wd.widget(this)
          onSuccess(wd)
        case _ => ;
      }
    }
    
    wclass.filter(Full(initData.getSingleton))
    
    dataToForm = (fu : (Box[String], (NodeSeq) => NodeSeq, (Any) => Unit) => NodeSeq) => fu(Empty, redoSnippet, onSubmit)
  
    if (! saved_?) <div id="widgetdata" >{dataToForm(initData.toForm _)}</div> else
    <div id="widgetdata" >{data.map(_.toForm(Empty, redoSnippet, onSubmit)) openOr Text("")}</div>
  }
  
  def aliasForm () : NodeSeq =
    knx_? match {
      case Full(w) =>
        <table>
          <tr>
          <td>
            KNX Groups
          </td>
          <td>
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
          </td>
          </tr>
        </table>
      case _ => Text("")
    }
  
  //creates a form for the widget and its data, as well as maybe KNX group selection
  def completeForm(submitText : String, onSuccess : (Widget, WidgetData[_]) => Unit, successRedirect : String) : NodeSeq = {
    def redo(ignore : NodeSeq) = completeForm(submitText, onSuccess, successRedirect)
    var wb : Box[Widget] = Empty
    var wdb : Box[WidgetData[_]] = Empty
    
    toForm(Empty, redo _, (nw : Widget) => wb = Full(nw)) ++
    dataForm(redo _, (nwd : WidgetData[_]) => wdb = Full(nwd)) ++
    aliasForm ++
    submit(submitText, () =>
      (wb, wdb) match {
        case (Full(w), Full(wd)) => onSuccess(w, wd); w.save; wd.widget(this); wd.save; S.redirectTo(successRedirect)
        case _ =>
      })
  }
  
  //creates a form for a new widget and its data, as well as maybe KNX group selection
  //has to be handled seperately because new widgets don't appear in the database
  def completeForm[Data <: WidgetData[Data]] (initData : Data, submitText : String, onSuccess : (Widget, Data) => Unit, successRedirect : String) : NodeSeq = {
    def redo(ignore : NodeSeq) = completeForm(initData, submitText, onSuccess, successRedirect)
    var wb : Box[Widget] = Empty
    var wdb : Box[Data] = Empty
    
    wclass.filter(Full(initData.getSingleton))
    
    toForm(Empty, redo _, (nw : Widget) => wb = Full(nw)) ++
    dataForm[Data](initData, redo _, (nwd : Data) => wdb = Full(nwd)) ++
    aliasForm ++
    submit(submitText, () =>
      (wb, wdb) match {
        case (Full(w), Full(wd)) => onSuccess(w : Widget, wd : Data); w.save; wd.widget(this); wd.save; S.redirectTo(successRedirect)
        case _ =>
      })
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
    if(! saved_?) Empty else WidgetList.map(wclass.is).data.find(By(WidgetList.map(wclass.is).data._widget, id.is))
  }
  
  override def delete_! = {
    data.map(_.delete_!)
    super.delete_!
  }
}
  
object Widget extends Widget with LongKeyedMetaMapper[Widget] {
  def roomless = findAll(By(room, Empty))
  
  override def afterSave = List(
    (w) => Distributor ! DBMessage(w.id.is)
  )
}
