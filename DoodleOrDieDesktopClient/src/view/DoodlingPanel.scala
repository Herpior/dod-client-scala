package view

import scala.swing._
import scala.swing.event._
import scala.swing.BorderPanel.Position._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import controller._
import dmodel.Magic
import dmodel.SizeModel
import dmodel.ColorModel
import io.Icons
import javax.swing.SwingUtilities

class DoodlingPanel(group_id:String,private_id:String,phrase:String,finish:Boolean,random:Boolean) extends BorderPanel with PlayPanel{

  val doodle = new DoodlePanel
  val skipButt = new Button{
    this.background = Magic.white
    this.foreground = Magic.buttColor
    this.opaque = true
    this.borderPainted = false
    //this.border = Swing.EmptyBorder//(0)
    //this.font = Magic.font20.deriveFont(15f)
    //this.contentAreaFilled = false
    //this.icon = Icons.getSkip
    this.action = Action("skip"){
    //this.icon = Icons.getSkip
      skip
      }}

  
                        controller.Timer(2*60*60*1000){
                          http.HttpHandler.ping
                        }.start
  
  /*val groups = http.HttpHandler.getGroupList.getGroups
  val roomChanger = new ComboBox(groups){
    this.background = Magic.white
    this.foreground = Magic.buttColor
    
    
    val index = groups.indexWhere (_._id==http.HttpHandler.getGroup)
    this.selection.index = if(index<0) 0 else index
    
    this.listenTo(this.selection)
    this.reactions += {
      case e:event.SelectionChanged =>
        changeGroup(selection.item._id)
    }
  }*/
  /*private def replaceWith(play:PlayPanel){
    this.publish(new ReplaceEvent(play,this))
  }*/
  
  //val txt = new TextField
  val desc = new PhrasePanel{
    this.contents += new BorderPanel{
      //this.contents += txt
      this.preferredSize = new Dimension(120,100)
      this.minimumSize = new Dimension(60,40)
      this.maximumSize = new Dimension(180,200)
      this.background = dmodel.Magic.white
      layout( skipButt ) = South
      layout(logoutButt) = North
      layout(new BoxPanel(Orientation.Vertical){
        contents += new Label("Room: ")
        this.background = Magic.white
        contents += roomChanger
      }) = Center
    }
  }
  private var tools = new ToolPanel
  val layers = new LayerPanel(doodle.model.layers)
  val extra = if(finish) "DRAW (last step): " else "DRAW: "
  desc.setPhrase( extra+phrase )//,extra)
  layout(layers) = West
  layout(desc) = North
  layout(doodle) = Center
  layout(tools) = East
  
  //val doodleControl = new DrawingController(doodle,tools,layers)
  //val toolControl = new ToolController(tools)
  
  doodle.model.loadFrom(private_id)
  //doodle.redrawAll
  //println("backup."+group_id/*http.HttpHandler.getGroup*/+".txt")
                //doodle.redrawMid
  
  this.listenTo(this.keys)
  this.listenTo(desc.keys)
  this.listenTo(roomChanger.keys)
  roomChanger.contents.foreach{x=>this.listenTo(x.keys)}
  this.listenTo(logoutButt.keys)
  //doodleControl.listenTo(txt)
  this.listenTo(skipButt.keys)
  this.listenTo(desc.phraseLabel.keys)
  
  layers.contents.foreach{x=>this.listenTo(x.keys)}
  layers.tools.contents.foreach{x=>this.listenTo(x.keys)}
  
  def listenToTools{
    tools.sizeP.contents.foreach{x=>this.listenTo(x.keys)}
    tools.sizeP.grid.contents.foreach{x=>this.listenTo(x.keys)}
    tools.colorP.contents.foreach{x=>this.listenTo(x.keys)}
    tools.toolP.contents.foreach{x=>this.listenTo(x.keys)}
  }  
  def deafToTools{
    tools.sizeP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.sizeP.grid.contents.foreach{x=>this.deafTo(x.keys)}
    tools.colorP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.toolP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.submitP.contents.foreach{x=>this.deafTo(x.keys)}
  }
  listenToTools
  this.listenTo(tools.submitP)
  
  
  def submit{
    if(tools.model.isReady){
      doodle.model.save
      this.publish(
          new view.ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      if(doodle.submit)
                        http.HttpHandler.state.toPlayPanel
                      else {
                        controller.Timer(100,false){
                          skipButt.requestFocusInWindow()
                        }.start
                        this
                      }
                  },this
              ),this
          )
      )
    }
  }
  
  override def logout {
    doodle.save
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
  
  val savetimer = Timer(10000,false){
    Future(doodle.save)
  }
  private var pinged = false
  private var check = 0l
  
  listenTo(layers.mouse.wheel)
  listenTo(layers.keys)
  listenTo(layers)
  listenTo(doodle.mouse.clicks)
  listenTo(doodle.mouse.moves)
  listenTo(doodle.mouse.wheel)
  listenTo(doodle.keys)
  listenTo(doodle)
  listenTo(tools.mouse.wheel)
  listenTo(tools.keys)
  listenTo(tools)
  reactions += {
    case e:RepaintEvent=>
      doodle.redrawAll
      doodle.repaint
      layers.reset
    case e:SubmitEvent=>
      this.submit
    /*case e:SizeChangeEvent =>
      doodle.model.setSize(e.size)
      doodle.repaint
    case e:ColorChangeEvent =>
      doodle.model.setColor(e.color,e.index)
    case e: ToolChangeEvent =>
      doodle.model.setState(e.tool)*/
    case e:MouseExited =>
      doodle.model.unselect
    case e:MouseWheelMoved =>
      val ctrl = e.peer.isControlDown()
      if(ctrl)doodle.zoomin(e.rotation*4)
      else doodle.zoomin(e.rotation)
      doodle.repaint
      //tools.model.zoomin(e.rotation)
          //repaint
    case e:KeyTyped =>
      /*  println(e.char)
      if(doodle.model.isWriting){
        //println(nexttext.cornerx)
        println(e.char)
        if(e.char!=''&&e.char!='')
          doodle.model.addChar(e.char)
      }*/
    case e:KeyPressed =>
      val alt = e.peer.isAltDown()
      val ctrl = e.peer.isControlDown()
      val shift = e.peer.isShiftDown()
      // shift = 64
      // ctrl = 128
      // alt =  512
      /*if(doodle.model.isWriting){
        //println(e.key)
        if(e.key == Key.BackSpace){
            doodle.model.backspace
          }
        else if(e.key==Key.Delete){
          doodle.model.deleteChar
        }
        else if(e.key==Key.V&&e.modifiers/128%2==1){
          val clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard
          val t = clipboard.getContents(this)
          if(t!=null&&t.getTransferDataFlavors.exists { x => x==java.awt.datatransfer.DataFlavor.stringFlavor}){
            try{
              doodle.model.addString(t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor).toString())//check this actually works
            //nexttext.text += t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor)
            } catch {
              case e =>{
                e.printStackTrace()
              }
            }
          }
        }
      } else*/
      e.key match {
        
        case Key.W =>
          ColorModel.colorUp
          tools.colorP.repaint()
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.A =>
          ColorModel.colorLeft
          tools.colorP.repaint()
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.S =>
          if(ctrl) {
            doodle.model.save//toLocalStorage
          }
          else {
            ColorModel.colorDown
            tools.colorP.repaint()
            //doodle.model.setColor(tools.model.getColor(0),0)
          }
        case Key.D =>
          ColorModel.colorRight
          tools.colorP.repaint()
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.Q =>
          SizeModel.sizeDown
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.E =>
          SizeModel.sizeUp
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key1 =>
          SizeModel.number(0)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key2 =>
          SizeModel.number(1)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key3 =>
          SizeModel.number(2)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key4 =>
          SizeModel.number(3)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key5 =>
          SizeModel.number(4)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key6 =>
          SizeModel.number(5)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key7 =>
          SizeModel.number(6)
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key8 =>
          if(Magic.authorized)SizeModel.number(7)
      doodle.model.unselect
          tools.sizeP.repaint()
            //doodle.model.setSize(tools.model.getSize)
        case Key.H =>
          if(Magic.authorized)tools.model.tool(4)//->perspect
      doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(8)
        case Key.M =>
          if(Magic.authorized && ctrl) {
            doodle.model.matrixLayer //TODO make this work better
            doodle.redrawAll
            doodle.repaint()
            layers.reset
          }
        case Key.J =>//->undefined
          if(Magic.authorized)tools.model.tool(5)
      doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(9)
        case Key.U =>
          tools.model.tool(1)//draw->line
      doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(0)
        case Key.I =>
          if(Magic.authorized)tools.model.tool(2)//line->bez
      doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(1)
        case Key.P =>
          if(ctrl){
            doodle.exportImage
          }
        case Key.C =>
          if(ctrl){
            //TODO: implement duplicate layer here
            //doodle.model.copy
          }
        /*
        case Key.V =>
          if(e.modifiers/128%2==1){
            doodle.model.paste
          }*/
        case Key.O =>
          if(Magic.authorized){
            if(ctrl){
              val text = Dialog.showInput(doodle, "file path", "open", Dialog.Message.Question, null, List[String](), "")
              text.foreach { x => 
                doodle.model.decryptFrom(x,private_id)
                doodle.redrawAll
                layers.reset
                //val t = new java.util.Scanner(new File(x))
                //while(t.hasNext()){
                //  layers.head.strokes.pushAll(LocalStorage.decrypt(t.next()))}
                }
            }
            else {
              tools.model.tool(3)//bezier->fill
      doodle.model.unselect
          tools.toolP.repaint()
              //doodle.model.setState(2)
            }
          }
        case Key.L =>
          if(Magic.authorized)tools.model.tool(7)//bezier2->undefined
      doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(7)
        //case Key.P =>
          //tools.model.tool(3)//gradient fill
          //doodle.model.setState(3)
        case Key.G =>
          if(alt){
            savetimer.stop()
            doodle.model.layers.split
            doodle.redrawAll
            Future(layers.reset)
            savetimer.start()
          }
        case Key.K =>
          if(Magic.authorized) tools.model.tool(6)//perspective set->undefined
          doodle.model.unselect
          tools.toolP.repaint()
          //doodle.model.setState(6)
        case Key.Enter =>
          if(ctrl){
            doodle.publish(new SubmitEvent)
            //HttpHandler.post(this.layers.flatMap { x => x.strokes.reverse.flatMap{y=>y.getLines} }.toArray)
          }
        case Key.Z =>
          if(!ctrl && !shift && !alt){
            doodle.fullScreen
            //val dir = if(zoom>2) 2 else -2
            //tools.model.zoomin(dir)
          }else if(ctrl && !shift){
            savetimer.stop()
            doodle.model.undo
            doodle.redrawMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          } else if(ctrl && shift){
            savetimer.stop()
            doodle.model.redo
            doodle.redrawLastMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          }
        case Key.Y =>
          if(ctrl){
            savetimer.stop()
            doodle.model.redo
            doodle.redrawLastMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          }
          else{
            tools.model.tool(0)//->draw
      doodle.model.unselect
          }
        case Key.F =>
          if(ctrl && !alt) {
            doodle.model.mergeLayer
            doodle.redrawMergeDown
            doodle.repaint
          }
          else if(alt && !ctrl){
            savetimer.stop()
            doodle.model.burn
            //doodle.model.toLocalStorage
            doodle.redrawMid
            doodle.repaint
            savetimer.start()
          }
          else {
            doodle.model.layerDown
            doodle.redrawLayerDown
          }
          Future(layers.reset)
        case Key.R =>
          if(ctrl) {
          //println("layerlist curr"+doodle.model.layers.ind)
            doodle.model.addLayer
            doodle.redrawLayerUp
          //println("layerlist curr"+doodle.model.layers.ind)
          }
          else {
            doodle.model.layerUp
            doodle.redrawLayerUp
          }
          Future(layers.reset)
        case _ =>
      }
      //repaint
    //case e:StateEvent =>
      //this.state = tools.model.bstate
    /*case e:ZoomEvent => 
      this.zoom = tools.model.zoomval
      //this.preferredSize = new Dimension(width,height)
      val tmp = current
      for(i<-layers.indices){
        current = i
        redraw
      }
      current = tmp*/
      
      //doodle.repaint
      //tools.repaint()
    case e:MouseMoved =>
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val alt = e.peer.isAltDown()
      val ctrl = e.peer.isControlDown()
      val shift = e.peer.isShiftDown()
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      tools.model.mouseMoved(doodle, place, ctrl, alt, shift)
      
      doodle.setCursor(e.point.getX, e.point.getY)
      //curx = e.point.getX.toInt
      //cury = e.point.getY.toInt
      doodle.repaint
      
    case e:MousePressed=> 
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val button = e.peer.getButton // 1 if left, 2 if middle, 3 if right
      val alt = e.peer.isAltDown()
      val ctrl = e.peer.isControlDown()
      val shift = e.peer.isShiftDown()
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      
      savetimer.stop()
      
      if(!pinged){
        http.HttpHandler.ping
        pinged = true
      }
      
      if(button == 1 && doodle.model.isMatrix){
        doodle.model.startMatrix(place, e.modifiers)
        doodle.redrawMid
      }
      else if(button == 2){ // middle button
        if(ctrl){ //TODO: implement layer moving here
        }
        else doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
      }
      else if(button > 1 && (alt || ctrl)){ // middle or right click with alt or ctrl, no left button down
        if(ctrl){
          if(Magic.authorized){
            val color = doodle.pickColor(e.point.getX.toInt,e.point.getY.toInt, shift)
            color.foreach(c=>ColorModel.setColor(c))
            tools.colorP.repaint()
          }
        } else if(alt){
          doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
        }
      }
      else tools.model.mousePressed(doodle, place, button, ctrl, alt, shift)
      
      
    case e:MouseReleased=>
      // ^left only = 0
      // ^middle only = 512
      // ^right only = 256
      // left = 1024
      // middle = 2048
      // right = 4096
      // shift = 64
      // ctrl = 128
      // alt =  512
      // alr gr = 640 = alt + ctrl
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val button = e.peer.getButton // 1 if left, 2 if middle, 3 if right
      val alt = e.peer.isAltDown()
      val ctrl = e.peer.isControlDown()
      val shift = e.peer.isShiftDown()
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      
      if(button == 1 && doodle.model.isMatrix){
        doodle.model.stopMatrix
        doodle.redrawMid
      }
      tools.model.mouseReleased(doodle, place, button, ctrl, alt, shift)
      
      doodle.model.addTime((System.nanoTime()-check)/100000)
      savetimer.start()
      Future(layers.reset)
      
    case e:MouseDragged=>
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val alt = e.peer.isAltDown()
      val ctrl = e.peer.isControlDown()
      val shift = e.peer.isShiftDown()
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()

      if(left && doodle.model.isMatrix){
        doodle.model.dragMatrix(place, e.modifiers)
        doodle.redrawMid
        doodle.repaint
      }
      tools.model.mouseDragged(doodle, place, left, middle, right, ctrl, alt, shift)

      doodle.setCursor(e.point.getX(), e.point.getY())
      doodle.repaint
  }
  
  /*private var fullscreen = false
  this.listenTo(doodle)
  reactions +={
    case e:FullscreenEvent=>
      if(fullscreen)this.publish(new ReplaceEvent(new FullscreenPanel(this),this))
      else 
  }*/
  /*def submit={
    ???
    true
  }
  def load={
    ???
    true
  }
  def skip={
    ???
    true
  }*/
  controller.Timer(100,false){
  skipButt.requestFocusInWindow()
  }.start
  
  this.focusable = true
  //roomChanger.
}
