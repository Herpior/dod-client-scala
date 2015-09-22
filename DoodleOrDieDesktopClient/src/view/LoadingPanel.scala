package view

/**
 * @author Herpior
 */

import swing._
import dmodel.Magic
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import controller.Timer

class LoadingPanel(real:Future[WindowPanel],old:WindowPanel) extends BoxPanel(Orientation.Vertical) with WindowPanel{
  
  real.onSuccess{case x=>
    this.publish(new ReplaceEvent(x,this))
    timer.stop()
    }
  real.onFailure{
    case e:org.apache.http.NoHttpResponseException =>
    this.publish(new ReplaceEvent(old,this))
      Dialog.showMessage(old, "Request timed out, please retry", e.getLocalizedMessage, Dialog.Message.Info, null)
  case e=>
    e.printStackTrace()
    this.publish(new ReplaceEvent(old,this))
  }
    //this.publish(new ReplaceEvent(x,this)) }
  
  
  this.background = Magic.bgColor
  val bordersize = new Dimension(300,80)
  this.contents += new BoxPanel(Orientation.NoOrientation){
    this.preferredSize=bordersize
    this.background = Magic.bgColor}
  val descrip = new PhrasePanel
  //desc.font = font20
  val loadingTexts = Array("Loading",
                           "Loading.",
                           "Loading..",
                           "Loading...",
                           "Still Loading...",
                           "Still Loading....",
                           "Still Loading.....",
                           "Still Loading......",
                           "Still Loading.......",
                           "This seems to take a while...",
                           "I'm sorry I can't make the servers any faster",
                           "Consider supporting Doodle or Die, maybe it'll help",
                           "Or maybe this program is just failing bad...",
                           "Anyways, hope this loads soon..")
  private var loadingIndex = 0
  descrip.setPhrase(loadingTexts(loadingIndex))
  loadingIndex += 1
  this.contents += new FlowPanel(descrip){this.background=Magic.bgColor}
  
  
  val timer:javax.swing.Timer= Timer(10000,false){
    descrip.setPhrase(loadingTexts(loadingIndex))
    repaint
    loadingIndex += 1
    if(loadingIndex<loadingTexts.length){
      timer.start()
      }
  }
  timer.start()
  
}