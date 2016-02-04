package view

import scala.swing._
import BorderPanel.Position._
import controller._
import dmodel.Magic
import io.Icons
import swing.event._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DoodlingPanel(group_id:String,private_id:String,phrase:String,finish:Boolean,random:Boolean) extends BorderPanel with PlayPanel{

  val doodle = new DoodlePanel
  val skipButt = new Button{
    this.background = Magic.white
    this.foreground = Magic.buttColor
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
  val tools = new ToolPanel
  val layers = new LayerListPanel(doodle.model.layers)
  val extra = if(finish) "DRAW (last step): " else "DRAW: "
  desc.setPhrase( extra+phrase )//,extra)
  layout(new ScrollPane(layers)) = West
  layout(desc) = North
  layout(doodle) = Center
  layout(new ScrollPane(tools)) = East
  
  //val doodleControl = new DrawingController(doodle,tools,layers)
  //val toolControl = new ToolController(tools)
  
  doodle.model.loadFrom("backup."+group_id/*http.HttpHandler.getGroup*/+".txt",private_id)
  //println("backup."+group_id/*http.HttpHandler.getGroup*/+".txt")
                //doodle.redrawMid
  
  this.listenTo(this.keys)
  this.listenTo(desc.keys)
  this.listenTo(roomChanger.keys)
  this.listenTo(logoutButt.keys)
  //doodleControl.listenTo(txt)
  this.listenTo(skipButt.keys)
  this.listenTo(desc.phraseLabel.keys)
  
  def submit{
    doodle.model.toLocalStorage
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
    case e:SubmitEvent=>
      this.submit
    /*case e:SizeChangeEvent =>
      doodle.model.setSize(e.size)
      doodle.repaint
    case e:ColorChangeEvent =>
      doodle.model.setColor(e.color,e.index)
    case e: ToolChangeEvent =>
      doodle.model.setState(e.tool)*/
    case e:MouseWheelMoved =>
      if(e.modifiers/128%2==1)doodle.zoomin(e.rotation)
      else doodle.zoomin(e.rotation*4)
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
          tools.model.colorup
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.A =>
          tools.model.colorleft
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.S =>
          if(e.modifiers == 128) doodle.model.toLocalStorage
          else {
            tools.model.colordown
            //doodle.model.setColor(tools.model.getColor(0),0)
          }
        case Key.D =>
          tools.model.colorright
            //doodle.model.setColor(tools.model.getColor(0),0)
        case Key.Q =>
          tools.model.sizedown
            //doodle.model.setSize(tools.model.getSize)
        case Key.E =>
          tools.model.sizeup
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key1 =>
          tools.model.number(0)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key2 =>
          tools.model.number(1)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key3 =>
          tools.model.number(2)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key4 =>
          tools.model.number(3)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key5 =>
          tools.model.number(4)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key6 =>
          tools.model.number(5)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key7 =>
          tools.model.number(6)
            //doodle.model.setSize(tools.model.getSize)
        case Key.Key8 =>
          if(Magic.authorized)tools.model.number(7)
            //doodle.model.setSize(tools.model.getSize)
        case Key.H =>
          if(Magic.authorized)tools.model.tool(4)//->perspect
          //doodle.model.setState(8)
        case Key.M =>
          //doodle.model.matrixLayer //TODO maybe add this eventually
        
        case Key.J =>//->undefined
          if(Magic.authorized)tools.model.tool(5)
          //doodle.model.setState(9)
        case Key.U =>
          tools.model.tool(1)//draw->line
          //doodle.model.setState(0)
        case Key.I =>
          if(Magic.authorized)tools.model.tool(2)//line->bez
          //doodle.model.setState(1)
        case Key.P =>
          if(e.modifiers/128%2==1){
            doodle.exportImage
          }
        /*case Key.C =>
          if(e.modifiers/128%2==1){
            doodle.model.copy
          }
        case Key.V =>
          if(e.modifiers/128%2==1){
            doodle.model.paste
          }*/
        case Key.O =>
          if(Magic.authorized){
            if(e.modifiers/128%2==1){
              val text = Dialog.showInput(doodle, "file path", "open", Dialog.Message.Question, null, List[String](), "")
              text.foreach { x => 
                doodle.model.loadFrom(x,private_id)
                doodle.redrawMid
                layers.reset
                //val t = new java.util.Scanner(new File(x))
                //while(t.hasNext()){
                //  layers.head.strokes.pushAll(LocalStorage.decrypt(t.next()))}
                }
            }
            else {
              tools.model.tool(3)//bezier->fill
              //doodle.model.setState(2)
            }
          }
        case Key.L =>
          if(Magic.authorized)tools.model.tool(7)//bezier2->undefined
          //doodle.model.setState(7)
        //case Key.P =>
          //tools.model.tool(3)//gradient fill
          //doodle.model.setState(3)
        case Key.G =>
          if(e.modifiers == 512){
            savetimer.stop()
            doodle.model.layers.split
            doodle.redrawTop
            Future(layers.reset)
            savetimer.start()
          }
        case Key.K =>
          if(Magic.authorized)tools.model.tool(6)//perspective set->undefined
          //doodle.model.setState(6)
        case Key.Enter =>
          if(e.modifiers /128%2==1){
            doodle.publish(new SubmitEvent)
            //HttpHandler.post(this.layers.flatMap { x => x.strokes.reverse.flatMap{y=>y.getLines} }.toArray)
          }
        case Key.Z =>
          if(e.modifiers==0){
            doodle.fullScreen
            //val dir = if(zoom>2) 2 else -2
            //tools.model.zoomin(dir)
          }else if(e.modifiers==128){
            savetimer.stop()
            doodle.model.undo
            doodle.redrawMid
            Future(layers.reset)
            savetimer.start()
          } else if(e.modifiers==192){
            savetimer.stop()
            doodle.model.redo
            doodle.redrawLastMid
            Future(layers.reset)
            savetimer.start()
          }
        case Key.Y =>
          if(e.modifiers == 128){
            savetimer.stop()
            doodle.model.redo
            doodle.redrawLastMid
            Future(layers.reset)
            savetimer.start()
          }
          else{
            tools.model.tool(0)//->draw
          }
        case Key.F =>
          if(e.modifiers==128) {
            doodle.model.mergeLayer
            doodle.redrawMergeDown
            doodle.repaint
          }
          else if(e.modifiers==512){
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
          if(e.modifiers==128) {
          //println("layerlist curr"+doodle.model.layers.ind)
            doodle.model.addLayer
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
      doodle.repaint
      tools.repaint()
    case e:MouseMoved =>
      /*if(tools.model.bstate == 1 && !next.strookes.isEmpty){
        dragLine(e)
      }*/
      //doodle.model.mouseMoved(place,e.modifiers)
      tools.model.getState match{
        /*case 2 =>
            val place = doodle.getCoord(e.point.getX, e.point.getY)
            val mods = e.modifiers
            doodle.model.dragBezier(3,place,mods)
            doodle.redrawDrawing*/
        case 2 =>//bezier2
          if(Magic.authorized){
              val place = doodle.getCoord(e.point.getX, e.point.getY)
              val mods = e.modifiers
              doodle.model.dragBezier(1,place,mods)
              doodle.model.dragBezier(2,place,mods)
              doodle.redrawDrawing
            }
        case _ =>
      }
      doodle.setCursor(e.point.getX, e.point.getY)
      //curx = e.point.getX.toInt
      //cury = e.point.getY.toInt
      doodle.repaint
      
    case e:MousePressed=> 
      savetimer.stop()
      if(!pinged){
        http.HttpHandler.ping
        pinged = true
      }
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      val mods = e.modifiers
      if((mods/1024)%2==1 && !doodle.model.isDrawing){
        //this.drawing = true
        check = System.nanoTime()
        tools.model.getState match {
          case 0 | 1 => //draw/line
            doodle.model.startLine(place,mods)
            doodle.redrawDrawing
            doodle.repaint
          case 2  =>//bezier
            if(Magic.authorized){
              if(!doodle.model.isBezier){
              //startLine(e)
              doodle.model.startBezier(place,mods)
              }
              else{
                //if(tools.model.getState == 2)doodle.model.dragBezier(3,place,mods)
                //else 
                  doodle.model.dragBezier(1,place,mods)
              }
              doodle.redrawDrawing
              doodle.repaint
            }
          case 3 =>//grad
            if(Magic.authorized)doodle.model.startGradient(place,mods)
          case 4 =>//setperspective
            if(Magic.authorized)doodle.model.dragPerspective(place,mods)
            /*if(mods /512%2==1){
              pers2 = Some(getX(e),getY(e))
            } else if(mods/128%2==1){
              pers3 = Some(getX(e),getY(e))
            } else pers = (getX(e),getY(e))*/
          case 5 =>
            //if(Magic.authorized)doodle.model.dragPerspective(place, mods)
            /*if(doodle.model.isWriting){
              doodle.model.stopWriting
              /*writing = false
              next = nexttext.toNextLinee
              nexttext = new textLine
              this.addStrooke
              next = new nextLinee*/
            } else {
              doodle.model.startWriting(place)
              /*
              writing = true
              val r = addText(e)
              nexttext.cornerx = r._1
              nexttext.cornery = r._2
              setText
              println("click "+nexttext.cornerx)*/
            }*/
          //  this.addStrooke
          case 6 =>
            //doodle.model.startMatrix(place,mods)
            /*
            val x = this.getX(e)
            val y = this.getY(e)
            if(mods/512%2==1){
                m2x = x
                m2y = y
            } else if(mods/128%2==1){
                m3x = x
                m3y = y
            } else if(mods/64%2==1){
                m0x = x
                m0y = y
            } else {
                m1x = x
                m1y = y
            }
            */
          case _ =>
        }
      }
      else if(mods /2048%2==1){
        doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
      }
      else if(mods/4096%2==1){
        
        tools.model.getState match{
          case 4 =>
            doodle.model.removePerspective(mods)
          case 1 =>
            doodle.model.addLine(place,mods)
            //doodle.model.dragLine(place,mods)
            doodle.redrawDrawing
            doodle.repaint
          case _ =>
            
        }
        if(mods/1024%2==0){
          if(mods/512%2==1){
            if(Magic.authorized){
              val color = doodle.pickColor(e.point.getX.toInt,e.point.getY.toInt,mods)
              color.foreach(c=>tools.model.setColor(c))
              tools.repaint
            }
          } else if(mods/128==1){
            doodle.prepareMove(dmodel.Coord(e.point.getX,e.point.getY))
          }
        }
        /*
        
          if (doodle.model.bstate == 6){//setperspective
            if(mods /512%2==1){
              pers2 = None
            } else if(mods/128%2==1){
              pers3 = None
            } else pers = (magicX/2,magicY/2)
          } else if(tools.model.bstate==1 && drawing){
            linePoly
          }*/
      }
    case e:MouseReleased=>
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      val mods = e.modifiers
      if(e.modifiers/1024%2==0 && doodle.model.isDrawing){
        //this.drawing = false
        tools.model.getState match {
          case 0|1 =>//draw//line
            doodle.model.stopLine(place,mods)
            doodle.redrawDrawing
            doodle.redrawLastMid
            doodle.repaint
          /*case 1 =>
            if(!next.strookes.isEmpty){
              addStrooke
          }*/
          /*case 2 =>//bezier
            if(!doodle.model.isBezier){
              doodle.model.dragBezier(1,place,mods)
              doodle.model.setBezier
            } else{
              doodle.model.dragBezier(2,place,mods)
              doodle.model.stopBezier
              doodle.redrawLastMid
            }
            doodle.redrawDrawing
            doodle.repaint*/
          case 3 =>//gradient fill
            if(Magic.authorized){
              if(e.modifiers/128%2 == 1)doodle.model.fillGradient(doodle.getSelected,place,mods)
              else doodle.model.addGradient(place,mods)
              doodle.redrawLastMid
              doodle.redrawDrawing
              doodle.repaint
            }
            //next = new nextLinee
          case 2 =>//bezier2
            if(Magic.authorized){
              if(!doodle.model.isBezier){
                doodle.model.dragBezier(3,place,mods)
                doodle.model.setBezier
                //bez = true
              } else{
                doodle.model.dragBezier(2,place,mods)
                doodle.model.stopBezier
                doodle.redrawLastMid
                //bez = false
              }
              //doodle.redrawMid
              doodle.redrawDrawing
              doodle.repaint
            }
          case 4 =>//perspective set
            if(Magic.authorized)doodle.model.dragPerspective(place, mods)
          case _ =>
        }
       doodle.model.addTime((System.nanoTime()-check)/10000)
       savetimer.start()
       Future(layers.reset)
      }
    case e:MouseDragged=>
      //savetimer.stop()
      val place = doodle.getCoord(e.point.getX, e.point.getY)
      val mods = e.modifiers
      if((e.modifiers/1024)%2==1){
        tools.model.getState match {
          case 0 =>//draw
            if((e.modifiers/512)%2==1){
              doodle.model.dragLine(place,mods)
              doodle.redrawDrawing
            }
            else {
              doodle.model.addLine(place,mods)
              doodle.redrawLast//Drawing
            }
            doodle.repaint
          case 1 =>//line
            doodle.model.dragLine(place,mods)
            doodle.redrawDrawing
            doodle.repaint
          /*case 2 =>//bezier
            if(doodle.model.isBezier){
              doodle.model.dragBezier(2,place,mods)
            } else {
              doodle.model.dragBezier(1,place,mods)
            }
            doodle.redrawDrawing
            doodle.repaint*/
          case 3 =>//gradient fill
            if(Magic.authorized){
              doodle.model.dragLine(place,0)
              doodle.redrawDrawing
              doodle.repaint
            }
          case 5 =>//pers
            if(Magic.authorized)doodle.model.dragPerspective(place, mods)
          //case 6 =>//perspective set
            //dragLine(e)
          case 2 =>//bezier2
            if(Magic.authorized){
              if(doodle.model.isBezier){
                doodle.model.dragBezier(2,place,mods)
              } else {
                doodle.model.dragBezier(3,place,mods)
              }
              doodle.redrawDrawing
              doodle.repaint
            }
          case _ =>
        }
      }
      else if(e.modifiers/2048%2==1){
        doodle.move(dmodel.Coord(e.point.getX, e.point.getY))
        doodle.redrawAll
      }
      else if((e.modifiers/4096)%2==1){
        /*if(tools.model.getState != 4 && (!doodle.model.isDrawing ||  tools.model.getState != 1)&&mods/128%2==1){
          
        doodle.move(dmodel.Coord(e.point.getX, e.point.getY))
        doodle.redrawAll
        }*/
      }
        //curx = e.point.getX.toInt
        //cury = e.point.getY.toInt
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