package view

import scala.swing.Component
import swing._
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import dmodel.Magic
import dmodel.JsonGroup

trait WindowPanel extends Component{
  //something here? nothing? okay..
}

trait PlayPanel extends WindowPanel {
  
  val logoutButt = new Button{
    this.background = Magic.white
    this.foreground = Magic.buttColor
    this.action = Action("logout"){
      //this.icon = Icons.getSkip
      logout
      }
  }
  
  val groups = Future(http.HttpHandler.getGroupList.getGroups)
    val tmp = new JsonGroup
    tmp.displayName = "Loading..."
    tmp._id = "global"
    
  private var combo = new ComboBox(List(tmp)){
    this.background = Magic.white
  }
  this.listenTo(combo.keys)
  val roomChanger = new BoxPanel(Orientation.NoOrientation){
    this.contents += combo
  }
  
  
  groups.onSuccess{case x=>
    
    roomChanger.contents --= roomChanger.contents
    combo = new ComboBox(x){
      this.background = Magic.white
    
      val index = x.indexWhere (_._id==http.HttpHandler.getGroup)
      this.selection.index = if(index<0) 0 else index
    
      this.listenTo(this.selection)
      this.listenTo(this.mouse.clicks)
      
      this.reactions += {
        //case e=>println(e.getClass)
        case e:event.SelectionChanged =>
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
  
  def logout {
    this.publish(
          new view.ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      http.HttpHandler.logout
                      new LogonPanel
                  },this
              ),this
          )
      )
  }
  
  def skip {
    val skips = http.HttpHandler.getSkips
    if(skips.skipsAvailable>0){
      val ok = Dialog.showConfirmation(this, "you have "+skips.skipsAvailable+" skips left. continue?", "skip", Dialog.Options.OkCancel, Dialog.Message.Question, null)
      if(ok == Dialog.Result.Ok){
        this.runSkip
      }
    } else {
      Dialog.showMessage(this, "You have no skips left", "skip", Dialog.Message.Warning, null)
    }
  }
  def runSkip {
      this.publish(
          new view.ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      http.HttpHandler.skip
                      http.HttpHandler.state.toPlayPanel
                  },this
              ),this
          )
      )
  }
  def changeGroup(group:String) {
      this.publish(
          new view.ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      http.HttpHandler.changeGroup(group)
                      http.HttpHandler.state.toPlayPanel
                  },this
              ),this
          )
      )
  }
  //def submit:Boolean
  //def load:Boolean
  //def skip:Boolean
  
}