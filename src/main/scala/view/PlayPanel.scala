package view

import controller.ReplaceEvent
import dmodel.{JsonGroup, JsonState, Magic}

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.swing.{Action, BoxPanel, Button, ComboBox, Dialog, Orientation, event}


object PlayPanel {
  def apply(state: JsonState): PlayPanel ={
    state.activeState match{
      case "draw"=>
        require(state.group_id!=null &&state.group_id.length>0)
        require(state.private_id!=null &&state.private_id.length>0)
        if(state.starting)state.phrase = "Start a new chain! Draw anything you want!"
        new DoodlingPanel(state.group_id,state.private_id,state.getPhrase,state.finishing)
      case "phrase"=>
        require(state.group_id!=null &&state.group_id.length>0)
        require(state.private_id!=null &&state.private_id.length>0)
        new PhrasingPanel(state.group_id,state.private_id,state.getDoodle,state.starting,state.finishing,state.randomizer)
      case _ =>
        new StalingPanel
    }
  }
}

trait PlayPanel extends WindowPanel {
  protected var pinged = false
  protected var last_ping:Long = 0
  val logoutButt: Button = new Button{
    this.background = Magic.white
    this.foreground = Magic.buttColor
    this.action = Action("logout "+Magic.user){
      //this.icon = Icons.getSkip
      logout()
    }
  }

  val groups = Future(
    try {
      http.HttpHandler.getGroupList.getGroups
    } catch {
      case _:NullPointerException=>Array[JsonGroup](new JsonGroup{displayName = "offline"})
    })
  val tmp = new JsonGroup
  tmp.displayName = "Loading..."
  tmp._id = "global"

  private var combo = new ComboBox(List(tmp)){
    this.background = Magic.white
  }
  this.listenTo(combo.keys)
  val roomChanger: BoxPanel = new BoxPanel(Orientation.NoOrientation){
    this.contents += combo
  }


  groups.onSuccess{case x=>

    roomChanger.contents --= roomChanger.contents
    combo = new ComboBox(x){
      this.background = Magic.white

      val index: Int = x.indexWhere (_._id==http.HttpHandler.getGroup)
      this.selection.index = if(index<0) 0 else index

      this.listenTo(this.selection)
      this.listenTo(this.mouse.clicks)

      this.reactions += {
        //case e=>println(e.getClass)
        case _:event.SelectionChanged =>
          changeGroup(selection.item._id)
      }
    }
    roomChanger.contents += combo
    this.listenTo(combo.keys)
    //this.publish(new SwitchRCEvent)
    //println(java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner())
    roomChanger.revalidate()

    this.listenTo(roomChanger)
    repaint
  }
  groups.onFailure{ case e =>
    e.printStackTrace()
    tmp.displayName = "Failed to Load"
  }

  def logout() {
    save()
    this.publish(
      new ReplaceEvent(
        new view.LoadingPanel(
          Future{
            http.HttpHandler.logout()
            new LogonPanel
          },this
        ),this
      )
    )
  }

  def skip() {
    val skips = http.HttpHandler.getSkips
    if(skips.skipsAvailable>0){
      val ok = Dialog.showConfirmation(this, "you have "+skips.skipsAvailable+" skips left. continue?", "skip", Dialog.Options.OkCancel, Dialog.Message.Question, null)
      if(ok == Dialog.Result.Ok){
        this.runSkip()
      }
    } else {
      Dialog.showMessage(this, "You have no skips left", "skip", Dialog.Message.Warning, null)
    }
  }
  def runSkip() {
    save()
    this.publish(
      new ReplaceEvent(
        new view.LoadingPanel(
          Future{
            http.HttpHandler.skip()
            PlayPanel(http.HttpHandler.state)
          },this
        ),this
      )
    )
  }
  def changeGroup(group:String) {
    save()
    this.publish(
      new ReplaceEvent(
        new view.LoadingPanel(
          Future{
            http.HttpHandler.changeGroup(group)
            PlayPanel(http.HttpHandler.state)
          },this
        ),this
      )
    )
  }
  def save()
  def ping(): Unit = {
    if (!Magic.offline){
      val f :Future[Boolean]  = Future(http.HttpHandler.ping)
      f.onSuccess{
        case b =>
          if(!b) {
            val reload = Dialog.showConfirmation(this, "The chain has timed out, do you want to reload", "Timed out",  Dialog.Options.YesNo, Dialog.Message.Warning, null)
            if (reload == Dialog.Result.Yes) {
              refresh()
            }
          }
      }
    }
    pinged = true
    last_ping = System.nanoTime()
  }
  def refresh() {
    this.publish(
      new ReplaceEvent(
        new view.LoadingPanel(
          Future(PlayPanel(http.HttpHandler.state)), this), this
      )
    )
  }
  def refreshLogoutButt() {
    this.logoutButt.text = "logout " + Magic.user
  }
  //def submit:Boolean
  //def load:Boolean
  //def skip:Boolean

}
