package view

/**
 * @author Herpior
 */

import swing._
import dmodel.Magic

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import controller.{ReplaceEvent, Timer}

class LoadingPanel(real:Future[WindowPanel],old:WindowPanel) extends BoxPanel(Orientation.Vertical) with WindowPanel{
  
  
  
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
                           "If the site is slow too, this might still go through",
                           "Consider supporting Doodle or Die, maybe it'll help",
                           "Or maybe this program is just failing bad...",
                           "Anyways, I'll let you back to the previous panel so you can retry this")
  private var loadingIndex = 0
  descrip.setPhrase(loadingTexts(loadingIndex))
  this.contents += new FlowPanel(descrip){this.background=Magic.bgColor}
  
  
  val timer:javax.swing.Timer= Timer(5000){
    loadingIndex += 1
    if(loadingIndex<loadingTexts.length) {
      descrip.setPhrase(loadingTexts(loadingIndex))
      repaint
    }
    else {
      this.publish(new ReplaceEvent(old,this))
      timer.stop()
    }
  }
  timer.start()
  
  def setResults() {
    real.onSuccess{
      case x=>
        timer.stop()
        this.publish(new ReplaceEvent(x,this))
      }
    real.onFailure{
      case e:org.apache.http.NoHttpResponseException =>
        timer.stop()
        this.publish(new ReplaceEvent(old,this))
        Dialog.showMessage(old, "Request timed out, please retry", e.getLocalizedMessage, Dialog.Message.Info, null)
      case e=>
        e.printStackTrace()
        timer.stop()
        this.publish(new ReplaceEvent(old,this))
    }
  }
  Timer(10, false) { //slow down so that the window has time to listen to this
    setResults
  }.start()
}