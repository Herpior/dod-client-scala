package view

import java.awt.Toolkit

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

class DoodlingPanel(group_id:String,private_id:String,phrase:String,finish:Boolean) extends BorderPanel with PlayPanel{

  //val derpPath = Magic..class.getProtectionDomain().getCodeSource().getLocation().getPath();
  //val decodedPath = java.io.URLDecoder.decode(derpPath, "UTF-8");
  private var pathname = "./saves/"+private_id+".png"
  private var filename = private_id
  private var savename = private_id
  
  val doodle = new DoodlePanel
  val skipButt: Button = new Button{
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
  val desc: PhrasePanel = new PhrasePanel{
    this.contents += new BorderPanel{
      //this.contents += txt
      this.preferredSize = new Dimension(200,100)
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
  private val tools = new ToolPanel
  val layers = new LayerFullPanel(doodle.model.layers)
  private val extra = if(finish) "DRAW (last step): " else "DRAW: "
  desc.setPhrase( extra+phrase )//,extra)
  layout(layers) = West
  if(!Magic.offline) layout(desc) = North
  layout(doodle) = Center
  layout(tools) = East
  
  //val doodleControl = new DrawingController(doodle,tools,layers)
  //val toolControl = new ToolController(tools)
  
  val lFrame = new DodFrame(layers, Unit=>swapLayerP())
  val tFrame = new DodFrame(tools, Unit=>swapToolP())
  private var istf = false
  private var islf = false
  def swapToolP() {
    if(istf){
      tFrame.deactivate
      layout(tools) = East
    }
    else {
      layout -= tools
      tFrame.activate
    }
    doodle.bufferer.redrawAll
    doodle.repaint
    this.revalidate()
    istf = !istf
  }
  def swapLayerP() {
    if(istf){
      lFrame.deactivate
      layout(layers) = West
    }
    else {
      layout -= layers
      lFrame.activate
    }
    doodle.bufferer.redrawAll
    doodle.repaint
    this.revalidate()
    istf = !istf
  }
  
  
  doodle.model.loadSave(private_id)
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
  desc.flow.contents.foreach{x=>this.listenTo(x.keys)}
  
  layers.contents.foreach{x=>this.listenTo(x.keys)}
  layers.tools.contents.foreach{x=>this.listenTo(x.keys)}
  
  def listenToTools(){
    tools.sizeP.contents.foreach{x=>this.listenTo(x.keys)}
    tools.sizeP.grid.contents.foreach{x=>this.listenTo(x.keys)}
    tools.colorP.contents.foreach{x=>this.listenTo(x.keys)}
    tools.toolP.contents.foreach{x=>this.listenTo(x.keys)}
  }  
  def deafToTools(){
    tools.sizeP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.sizeP.grid.contents.foreach{x=>this.deafTo(x.keys)}
    tools.colorP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.toolP.contents.foreach{x=>this.deafTo(x.keys)}
    tools.submitP.contents.foreach{x=>this.deafTo(x.keys)}
  }
  listenToTools()
  this.listenTo(tools.submitP)
  
  def export(saveAs:Boolean) {
    val text = Dialog.showInput(doodle, "set percentage", "percentage of exported image size compared to the image size "+Magic.doodleSize.x+"x"+Magic.doodleSize.y+" px", Dialog.Message.Question, null, List[String](), "100%")
    text.foreach { x => 
      try {
        val xtrim = x.takeWhile { x => x == '.' || (x >= '0' && x <= '9') }
        val percent = xtrim.toDouble/100
        if(filename == "offline" || saveAs) {
          val fc = new FileChooser //Dialog.showInput(doodle, "set file name", "exported image name", Dialog.Message.Question, null, List[String](), "offline")
          //fc.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
          fc.fileFilter = new io.PngFilter
          fc.title = "export file"
          fc.peer.setCurrentDirectory(new java.io.File(this.pathname).getParentFile)
          val res = fc.showSaveDialog(this)
          if (res != FileChooser.Result.Approve) return
          val file = fc.selectedFile
          filename = file.getName
          pathname = file.getAbsolutePath
          if(!pathname.endsWith("png")){
            pathname = pathname + ".png"
          }
          if(filename.endsWith("png")) {
            filename = filename.dropRight(4)
          }
          doodle.exportImage(percent, pathname)
          //}
        }
        else doodle.exportImage(percent, pathname)
      }
      catch {
        case e:Throwable => 
      }
    }
  }
  
  private def submit(){
    if(Magic.offline) {
      export(false)
      save
    }
    else if(tools.model.isReady){
      save
      this.publish(
          new ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      if(doodle.submit)
                        http.HttpHandler.state.toPlayPanel
                      else {
                        controller.Timer(100, false) {
                          skipButt.requestFocusInWindow()
                        }.start()
                        this
                      }
                  },this
              ),this
          )
      )
    }
  }
  def save {
    doodle.save(savename)
    val curr_time = System.nanoTime()
    if(last_ping < curr_time - 7200000000000L) { // two hours between pings
      ping
    }
  }
  def load() {
    val fc = new FileChooser //Dialog.showInput(doodle, "set file name", "exported image name", Dialog.Message.Question, null, List[String](), "offline")
    //fc.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    fc.fileFilter = new io.TxtFilter
    fc.title = "load save"
    fc.peer.setCurrentDirectory(new java.io.File(this.pathname).getParentFile)
    val res = fc.showOpenDialog(this)
    if (res != FileChooser.Result.Approve) return
    val file = fc.selectedFile
    doodle.model.decryptFrom(file.getAbsolutePath)
    doodle.bufferer.redrawAll
    layers.reset
    if(savename == "offline"){
      val yesNo = Dialog.showConfirmation(doodle, "Do you want to use this file as save location?", "Set as save location")
      if(yesNo == Dialog.Result.Yes){
        val fname = file.getName
        savename = fname.replace("save.", "").replace(".txt", "")
      }
    }
  }
  
  override def logout {
    save
    this.publish(
          new ReplaceEvent(
              new view.LoadingPanel(
                  Future{
                      http.HttpHandler.logout
                      new LogonPanel
                  },this
              ),this
          )
      )
  }

  private val savetimer = Timer(10000,repeats = false){
    Future(save)
  }
  //
  private var check = 0l
  
  listenTo(ConfigDrawingPanel)
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
  tools.toolConfigPs.foreach(
    cf=>{
      listenTo(cf.keys)
      listenTo(cf)
      cf.configContentsThatLikeToStealFocus.foreach(c=>listenTo(c.keys))
    })
  listenTo(tools.toolP) //toolchange
  reactions += {
    case e:ToolChangeEvent=>
      doodle.bufferer.redrawDrawing
      doodle.repaint
    case e:RepaintEvent=>
      doodle.bufferer.redrawAll
      doodle.repaint
      layers.reset
    case e:SubmitEvent=>
      this.submit()
    /*case e:SizeChangeEvent =>
      doodle.model.setSize(e.size)
      doodle.repaint
    case e:ColorChangeEvent =>
      doodle.model.setColor(e.color,e.index)
    case e: ToolChangeEvent =>
      doodle.model.setState(e.tool)*/
    case e:MouseExited =>
      //doodle.model.unselect
      //TODO: add support for mouse exited for tools
    case e:MouseWheelMoved =>
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      if(ctrl)doodle.bufferer.zoomIn(e.rotation*4)
      else doodle.bufferer.zoomIn(e.rotation)
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
      val alt = e.peer.isAltDown
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      val shift = e.peer.isShiftDown
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
          //ColorModel.colorUp
          tools.colorP.colorUp
        case Key.A =>
          //ColorModel.colorLeft
          tools.colorP.colorLeft//.repaint()
        case Key.S =>
          if(ctrl) {
            if(this.savename == "offline"){
              val newSaveName = Dialog.showInput(doodle, "set save name", "The name of the save file. So you don't have to save everything to save.offline.txt", Dialog.Message.Question, null, List[String](), "untitled")
              newSaveName.foreach(savename = _)
              save
            }
            else{
              save
            }//toLocalStorage
          }
          else {
            //ColorModel.colorDown
            tools.colorP.colorDown//repaint()
          }
        case Key.D =>
          //ColorModel.colorRight
          tools.colorP.colorRight//repaint()
          
        case Key.Q =>
          SizeModel.sizeDown
          tools.sizeP.repaint()
        case Key.E =>
          SizeModel.sizeUp
          tools.sizeP.repaint()
          
        case Key.Key1 =>
          SizeModel.number(0)
          tools.sizeP.repaint()
        case Key.Key2 =>
          SizeModel.number(1)
          tools.sizeP.repaint()
        case Key.Key3 =>
          SizeModel.number(2)
          tools.sizeP.repaint()
        case Key.Key4 =>
          SizeModel.number(3)
          tools.sizeP.repaint()
        case Key.Key5 =>
          SizeModel.number(4)
          tools.sizeP.repaint()
        case Key.Key6 =>
          SizeModel.number(5)
          tools.sizeP.repaint()
        case Key.Key7 =>
          SizeModel.number(6)
          tools.sizeP.repaint()
        case Key.Key8 =>
          if(Magic.authorized)SizeModel.number(7)
          tools.sizeP.repaint()

        case Key.U =>
          tools.setTool(1)//->line
        case Key.I =>
          tools.setTool(2)//->bez
        case Key.H =>
          tools.setTool(4)//->perspect
        case Key.J =>
          tools.setTool(5)//->zoom
        case Key.K =>
          tools.setTool(6)//->hand
        case Key.L =>
          tools.setTool(7)//->undefined
          
        case Key.C =>
          if(ctrl){
            //doodle.model.copy
          }
          else {
            ConfigDrawingPanel.activate
          }
        /*
        case Key.V =>
          if(e.modifiers/128%2==1){
            doodle.model.paste
          }*/
        case Key.M =>
          if(Magic.authorized && ctrl) {
            doodle.model.matrixLayer
            doodle.bufferer.redrawAll
            doodle.repaint()
            layers.reset
          }
        case Key.O =>
          if(Magic.authorized){
            if(ctrl){
              load
            }
            else {
              tools.setTool(3)//->fill
            }
          }
        
        case Key.P =>
          if(ctrl){
            export(true)
          }
          else {
            export(false)
          }
        case Key.G =>
          if(alt){
            savetimer.stop()
            doodle.model.layers.split
            doodle.bufferer.redrawAll
            doodle.repaint
            Future(layers.reset)
            savetimer.start()
          }
        case Key.Enter =>
          if(ctrl){
            this.submit()//doodle.publish(new SubmitEvent)
            //HttpHandler.post(this.layers.flatMap { x => x.strokes.reverse.flatMap{y=>y.getLines} }.toArray)
          }
        case Key.Z =>
          if(!ctrl && !shift && !alt){
            //doodle.fullScreen
            //val dir = if(zoom>2) 2 else -2
            //tools.model.zoomin(dir)
          }else if(ctrl && !shift){
            savetimer.stop()
            doodle.model.undo
            doodle.bufferer.redrawMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          } else if(ctrl && shift){
            savetimer.stop()
            doodle.model.redo
            doodle.bufferer.redrawLastMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          }
        case Key.Y =>
          if(ctrl){
            savetimer.stop()
            doodle.model.redo
            doodle.bufferer.redrawMid
            doodle.repaint()
            Future(layers.reset)
            savetimer.start()
          }
          else{
            tools.setTool(0)//->draw
          }
        case Key.F =>
          if(ctrl && !alt) {
            doodle.model.mergeLayer
            doodle.bufferer.redrawMergeDown
            doodle.repaint
          }
          else if(alt && !ctrl){
            savetimer.stop()
            doodle.model.burn
            //doodle.model.toLocalStorage
            doodle.bufferer.redrawMid
            doodle.repaint
            savetimer.start()
          }
          else {
            doodle.model.layerDown
            doodle.bufferer.redrawLayerDown
            doodle.repaint()
          }
          Future(layers.reset)
        case Key.R =>
          if(ctrl) {
          //println("layerlist curr"+doodle.model.layers.ind)
            doodle.model.addLayer
            doodle.bufferer.redrawLayerUp
            doodle.repaint()
          //println("layerlist curr"+doodle.model.layers.ind)
          }
          else {
            doodle.model.layerUp
            doodle.bufferer.redrawLayerUp
            doodle.repaint()
          }
          Future(layers.reset)
        case Key.B =>
          if(ctrl) this.swapLayerP()
          else this.swapToolP()
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
      val place = doodle.bufferer.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val alt = e.peer.isAltDown
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      val shift = e.peer.isShiftDown
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      if(doodle.bufferer.canDraw){
        tools.model.mouseMoved(doodle, place, ctrl, alt, shift)
      }
      
      doodle.setCursor(e.point.getX, e.point.getY)
      //curx = e.point.getX.toInt
      //cury = e.point.getY.toInt
      doodle.repaint
      
    case e:MousePressed=> 
      val place = doodle.bufferer.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val button = e.peer.getButton // 1 if left, 2 if middle, 3 if right
      val alt = e.peer.isAltDown
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      val shift = e.peer.isShiftDown
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      
      savetimer.stop()
      
      if(!pinged){
        ping
      }
      
      if(button == 1 && doodle.model.isMatrix){
        doodle.model.startMatrix(place, e.modifiers)
        doodle.bufferer.redrawMid
      }
      else if(button == 2){ // middle button
        if(ctrl){ //TODO: implement layer moving here?
        }
        else tools.model.handTool.onMouseDown(doodle.bufferer, place, button, ctrl, alt, shift)//doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
      }
      else if(button > 1 && alt && !tools.model.isBusy ){ // middle or right click with alt or ctrl, no left button down
          if(Magic.authorized){
            val color = doodle.bufferer.pickColor(e.point.getX.toInt,e.point.getY.toInt, shift)
            color.foreach(c=>tools.colorP.setColor(c))
            tools.colorP.repaint()
          }
        /*else if(alt){
          dmodel.tools.HandTool.onMouseDown(doodle, place, button, ctrl, alt, shift) //doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
        }*/
      }
      else if(doodle.bufferer.canDraw){
        tools.model.mousePressed(doodle, place, button, ctrl, alt, shift)
      }
      check = System.nanoTime()
      doodle.repaint()
      
    case e:MouseReleased=>
      // ^left only = 0
      // ^middle only = 512
      // ^right only = 256
      // left = 1024
      // middle = 2048
      // right = 4096
      // shift = 64
      // ctrl = 128
      // cmd = 256
      // alt =  512
      // alr gr = 640 = alt + ctrl
      val place = doodle.bufferer.getCoord(e.point.getX, e.point.getY)
      //val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      //val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      //val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val button = e.peer.getButton // 1 if left, 2 if middle, 3 if right
      val alt = e.peer.isAltDown
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      val shift = e.peer.isShiftDown
      //print(e.modifiers & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
      //print(" ")
      //print(e.peer.getButton)
      //print(" ")
      //println(e.modifiers)
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()
      
      if(button == 1 && doodle.model.isMatrix){
        doodle.model.stopMatrix
        doodle.bufferer.redrawMid
      }
      else if(button == 2){
        tools.model.handTool.onMouseUp(doodle.bufferer, place, button, ctrl, alt, shift)
      }
      else if(doodle.bufferer.canDraw){
        tools.model.mouseReleased(doodle, place, button, ctrl, alt, shift)
      }

      doodle.model.addTime(((System.nanoTime()-check)/100000).toInt)

      savetimer.start()
      doodle.repaint()
      Future(layers.reset)
      
    case e:MouseDragged=>
      val place = doodle.bufferer.getCoord(e.point.getX, e.point.getY)
      val left = javax.swing.SwingUtilities.isLeftMouseButton(e.peer)
      val middle = javax.swing.SwingUtilities.isMiddleMouseButton(e.peer)
      val right = javax.swing.SwingUtilities.isRightMouseButton(e.peer)
      val alt = e.peer.isAltDown
      val ctrl = e.peer.isControlDown || e.modifiers == 256
      val shift = e.peer.isShiftDown
      //val altgr = e.peer.isAltGraphDown()
      //val meta = e.peer.isMetaDown()

      if(left && doodle.model.isMatrix){
        doodle.model.dragMatrix(place, e.modifiers)
        doodle.bufferer.redrawMid
        doodle.repaint
      }
      else if(middle){
        tools.model.handTool.onMouseDrag(doodle.bufferer, place, left, middle, right, ctrl, alt, shift)
      }
      else if(doodle.bufferer.canDraw){
        tools.model.mouseDragged(doodle, place, left, middle, right, ctrl, alt, shift)
      }

      doodle.setCursor(e.point.getX, e.point.getY)
      doodle.repaint
    //case e:Event => println(e)
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
  controller.Timer(100, false) {
    tools.sizeP.contents.head.requestFocusInWindow()
  }.start()
  
  this.focusable = true
  //roomChanger.
}
