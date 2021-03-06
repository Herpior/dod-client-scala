package view

import controller.ReplaceEvent

import scala.swing._
import BorderPanel.Position._
import dmodel.Magic
import dmodel.json.JsonDoodle

import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import io.Icons

class PhrasingPanel(group_id:String,private_id:String,doodle:JsonDoodle,start:Boolean,finish:Boolean,random:Boolean) extends BorderPanel with PlayPanel{

  val this_phrasing_panel: PhrasingPanel = this
  this.background = Magic.bgColor
  val skipButt: Button = new Button{
    this.background = Magic.white
    this.foreground = Magic.buttColor
    this.opaque = true
    this.borderPainted = false
    this.action = Action("skip"){
      //this.icon = Icons.getSkip
      skip
      }}
  //val random = new RandomPrompt
  /*val groups = http.HttpHandler.getGroupList.getGroups
  val roomChanger = new ComboBox(groups){
    this.background = Magic.white
    
    val index = groups.indexWhere (_._id==http.HttpHandler.getGroup)
    this.selection.index = if(index<0) 0 else index
    
    this.listenTo(this.selection)
    this.reactions += {
      case e:event.SelectionChanged =>
        http.HttpHandler.changeGroup(selection.item._id)
        replaceWith(http.HttpHandler.state.toPlayPanel)
    }
  }
  private def replaceWith(play:PlayPanel){
    this.publish(new ReplaceEvent(play,this))
  }*/
  
  val describe: PhrasePanel = new PhrasePanel{
    this.contents += new BorderPanel{
      //this.contents += txt
      this.preferredSize = new Dimension(200,100)
      this.minimumSize = new Dimension(60,50)
      this.maximumSize = new Dimension(160,200)
      this.background = dmodel.Magic.white
      layout( skipButt ) = South
      layout(logoutButt) = North
      layout(new BoxPanel(Orientation.Vertical){
        contents += new Label("Room:")
        this.background = Magic.white
        contents += roomChanger
      }) = Center
    }
  }
  val desc: String = if(start) "What would you like the next player to draw?" else if(finish) "Describe the doodle. (last step)" else "Describe the doodle."
  describe.setPhrase(desc)
  
  
  val doodleP: DoodlePanel = new DoodlePanel{ //TODO: the drawing is placed in the corner
    this.preferredSize = new Dimension(Magic.x,Magic.y)
    this.minimumSize = new Dimension(Magic.x,Magic.y)
    this.maximumSize = new Dimension(Magic.x,Magic.y)
    this.bufferer.setBounds(Magic.x,Magic.y)
  }
  val real = new TextArea
  val f = Future{
    doodleP.model.load(doodle)
    doodleP.bufferer.redrawAll
    doodleP.repaint()
    real.requestFocusInWindow()
  }
  val limit: Label = new Label(Magic.maxChars.toString){
    this.foreground = Magic.buttColor
    this.font = Magic.font20
  }
  //real.
  //real.visible = false
  
  this.listenTo(real.keys)
  reactions += {
    case e:event.KeyReleased =>
      if(!pinged){
        ping
      }
      if(real.text.length()>Magic.maxChars){
        real.text = real.text.dropRight(real.text.length-Magic.maxChars)
        }
      limit.text = (Magic.maxChars-real.text.length).toString
      
  }
  
  val submitButt: Button = new Button("submit"){
    this.font = Magic.font20
    this.background = Magic.buttColor
    this.foreground = Magic.white
    this.opaque = true
    this.borderPainted = false
    this.action = new Action("Submit"){
      def apply(): Unit =submit
    }
  }
  val editing: BoxPanel = new BoxPanel(Orientation.Vertical){
    this.contents+= new PhrasePanel{
      this.background = Magic.bgColor
      this.preferredSize = new Dimension(Magic.x,Magic.y-100)
      this.minimumSize = this.preferredSize
      this.maximumSize = this.preferredSize
      //this.contents -= this.desc
      this.desc.preferredSize=new Dimension(Magic.x-60,Magic.y-160)
      this.desc.minimumSize = new Dimension(300,200)
      this.desc.maximumSize = new Dimension(600,400)
      
      this.desc.contents.clear()
      this.desc.contents += real
      this.desc.contents += new BorderPanel{
        this.background = Magic.white
        layout(limit) = East
      }
      real.font = Magic.font20
      
      real.peer.setWrapStyleWord(true)
      real.peer.setLineWrap(true)
      //real.preferredSize = new Dimension
    }
    this.contents += new FlowPanel(submitButt){
      this.background = Magic.bgColor
    }
    
    
  }
  
  /*reactions += {
    case e=>println(e.getClass)
  }*/
  
  layout(describe)=North
  layout(new FlowPanel(doodleP,editing){this.background=Magic.bgColor})=Center
  
  def save {}
  def submit(){
    if(real.text.length()<6){
      Dialog.showMessage(this, "The description is too short", "Error", Dialog.Message.Error, null)
      return
    }
    this.publish(
          new ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      if(http.HttpHandler.submitDesc(real.text)){
                        PlayPanel(http.HttpHandler.state)
                      }
                      else {
                        this_phrasing_panel
                      }
                  },this_phrasing_panel
              ),this_phrasing_panel
          )
      )
  }
  /*
  def load={
    ???
    true
  }
  def skip={
    ???
    true
  }*/
}