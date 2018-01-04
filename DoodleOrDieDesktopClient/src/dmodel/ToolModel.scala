package dmodel
import dmodel.tools._
import view.DoodlePanel

object ToolModel {

  private var mini = false
  private var tool:BasicTool = DrawTool
  private var state = 0
  private var ready = Magic.readyDefault
  private var toolList:Array[BasicTool] = Array(DrawTool, LineTool, BezierTool, FillTool)
  
  def isReady = this.ready
  def initReady {ready = Magic.readyDefault}
  
  def tool(n:Int){
    if(n>=0 && n<=8 && (n<2||Magic.authorized)) {
      state=n
      tool = toolList(n)
    }
    //TODO: change this part completely, to use the tool objects
  }
  def getState = state
  def getTool = tool
  
  
  def clickReady(){
    ready = !ready
  }
  
  def modDecrypt(mod:Int) = {
    val alt = mod/64%2
  }
  
  def mouseMoved(dp:DoodlePanel, point:Coord, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseMove(dp, point, ctrl, alt, shift)
    /*tools.model.getState match{
        /*case 2 =>
            val place = doodle.getCoord(e.point.getX, e.point.getY)
            val mods = e.modifiers
            doodle.model.dragBezier(3,place,mods)
            doodle.redrawDrawing*/
        case 2 =>//bezier2
          
        case 7 =>//edit
          if(Magic.authorized){
              val place = doodle.getCoord(e.point.getX, e.point.getY)
              val mods = e.modifiers
              doodle.model.select(place,mods,true)
            }
        case _ =>
      }*/
  }
  
  def mousePressed(dp:DoodlePanel, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDown(dp, point, button, ctrl, alt, shift)
    /*
      // left = 1024
      // right = 4096
      // middle = 2048
      // shift = 64
      // ctrl = 128
      // alt = 512
     
      else if(left && !doodle.model.isDrawing){
        //this.drawing = true
        check = System.nanoTime()
         
        tools.model.getState match {
          case 0 | 1 => //draw/line
            
          case 2  =>//bezier
            
          case 3 =>//grad
            
          case 4 =>//setperspective
            if(Magic.authorized)doodle.model.dragPerspective(place,mods)
            /*if(mods /512%2==1){
              pers2 = Some(getX(e),getY(e))
            } else if(mods/128%2==1){
              pers3 = Some(getX(e),getY(e))
            } else pers = (getX(e),getY(e))*/
          case 7 =>//edit
          if(Magic.authorized){
              val place = doodle.getCoord(e.point.getX, e.point.getY)
              val mods = e.modifiers
              doodle.model.select(place,mods,false)
            }
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
          //case 6 =>
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
      
      else if(mods/4096%2==1){
        
        tools.model.getState match{
          case 4 =>
            doodle.model.removePerspective(mods)
          case 1 =>
          case _ =>
            
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
     */
  }
  
  def mouseReleased(dp:DoodlePanel, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseUp(dp, point, button, ctrl, alt, shift)
    /*
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
      
      else if(e.modifiers/1024%2==0 && doodle.model.isDrawing){
        //this.drawing = false
        tools.model.getState match {
          case 0|1 =>//draw//line
           
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
            
            //next = new nextLinee
          case 2 =>//bezier2
            
          case 4 =>//perspective set
            if(Magic.authorized){
              doodle.model.dragPerspective(place, mods)
              }
          case 7 =>//line fill
            if(Magic.authorized){
              //println("auth line fill panel")
              //val place = doodle.getCoord(e.point.getX, e.point.getY)
              //val mods = e.modifiers
              doodle.model.lineFill(mods)
              doodle.redrawLastMid
            }
          case _ => 
            println("case x doodling panel")
        }
     */
  }
  
  def mouseDragged(dp:DoodlePanel, point:Coord, left:Boolean, middle:Boolean, right:Boolean, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDrag(dp, point, left, middle, right, ctrl, alt, shift)
    /*
     
      
      else if((mods/1024)%2==1){
        tools.model.getState match {
          case 0 =>//draw
            
          case 1 =>//line
            
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
          case 4 =>//pers
            if(Magic.authorized)doodle.model.dragPerspective(place, mods)
          case 7 =>//edit
            if(Magic.authorized){
              val place = doodle.getCoord(e.point.getX, e.point.getY)
              val mods = e.modifiers
              doodle.model.select(place,mods,false)
            }
          case 2 =>//bezier2
            
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
     */
  }
}