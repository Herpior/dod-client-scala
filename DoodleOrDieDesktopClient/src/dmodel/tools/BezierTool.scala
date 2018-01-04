package dmodel.tools
import dmodel.Angle
import dmodel.Magic
import dmodel.BasicLine
import dmodel.BezierLine
import dmodel.ColorModel
import dmodel.Colors
import dmodel.Coord
import dmodel.DoodleModel
import dmodel.MultiLine
import dmodel.Perspective
import dmodel.SizeModel
import math.Pi
import view.DoodlePanel

object BezierTool extends LineToolClass {
  
  override def onMouseMove(dp:DoodlePanel, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    if(Magic.authorized && state == 2){
      dragBezier(1, coord, control, shift)
      dragBezier(2, coord, control, shift)
      dp.redrawDrawing
    }
  }
  override def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    if(Magic.authorized && left){
      if(state == 1){
        dragBezier(3, coord, control, shift)
      } else {
        dragBezier(2, coord, control, shift)
      }
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def onMouseUp(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(Magic.authorized && button == 1){
      if(state == 1){
        dragBezier(3, coord, control, shift)
        state = 2
      } else {
        dragBezier(2, coord, control, shift)
        stopBezier(dp.model)
        dp.redrawLastMid
        state = 0
      }
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(Magic.authorized && button == 1){
      if(state == 0){
      //startLine(e)
      startBezier(coord)
      state = 1
      }
      else{
        dragBezier(1, coord, control, shift)
        state = 3
      }
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def getLines() = {
    val buf = bezierLine.getLines ++ guideLine.getLines
    buf.toBuffer
  } //for redrawing the whole line while drawing?
  override def getLastLine() = {None} // for drawing one segment of the line
  
  // state 0 = nothing ongoing
  // state 1 = mouse down first time, act like drawing straight line
  // state 2 = mouse up firsts time, line follows on move
  // state 3 = mouse down second time, moving the last
  private var state = 0
  private var bezierLine: BezierLine = new BezierLine(ColorModel.getColor,SizeModel.getSize)
  private var guideLine: MultiLine = new MultiLine
  
  //def initTool() {}
  //def onMouseUp(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseMove(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  
  //def setBezier{
  //  this.bezier = true
  //}

  
  def startBezier(place:Coord){
    //val bez = new BezierLine(ColorModel.getColor,SizeModel.getSize)
    //val guide = new MultiLine
    val bez = bezierLine
    val guide = guideLine
    bez.color = ColorModel.getColor
    bez.size = SizeModel.getSize
    val inverse = Colors.inverse(bez.color)
    guide.addLine( new BasicLine(inverse,0.1) )
    val s = guide.getLines.head
    for(i <- 0 to 3){
      bez.setCoord(i,place)
      s.addCoord(place)
    }
    //model.startDrawing(Array(bez, guide))
  }
  /*def addBezPt(pt:Int,next:BezierLine,guide:MultiLine,place:Coord,mods:Int){
   /* require(pt>=0 && pt <= 3)
    val x = getX(e)
    val y = getY(e)
    nextbez.foreach( _.xs(pt)=x)
    nextbez.foreach( _.ys(pt)=y)
    val s = next.strookes(0)
    s.xs(pt) = x
    s.ys(pt) = y
    */
    dragBezier(pt,e)
  }*/
  def dragBezier(pt:Int, place:Coord, ctrl:Boolean, shift:Boolean){
    require(pt>=0 && pt <= 3)
    var coord = place
      if(ctrl){
          pt match{
            case 0 =>
            case 1 | 3 =>
              val c0 = bezierLine.getCoord(0)
              val dc = place-c0
              val len = place.dist(c0)
              val xy = 
                if (shift) Perspective.getCoord(c0,Angle.angle(dc.x,dc.y),len)
                else Angle.getCoord(math.round(Angle.angle(dc.x,dc.y)/Pi*4)*Pi/4,len)
              coord = c0+xy
            case 2 =>
              val c0 = bezierLine.getCoord(3)
              val dc = place-c0
              val len = place.dist(c0)
              val xy = 
                if (shift) Perspective.getCoord(c0,Angle.angle(dc.x,dc.y),len)
                else Angle.getCoord(math.round(Angle.angle(dc.x,dc.y)/Pi*4)*Pi/4,len)
              coord = c0+xy
            case _ =>
              //println("you dun fucked up dragbezier pt: "+pt)
          }
        }
        bezierLine.setCoord(pt, coord)
        val s = guideLine.getLines.head
        if(pt==3){
          bezierLine.setCoord(2,coord)
          s.setCoord(2,coord)
        }
        s.setCoord(pt,coord)
          
  }
  
  
  def stopBezier(model:DoodleModel){
    //this.bezier = false
    model.layers.getCurrent.add(bezierLine)
    bezierLine = new BezierLine(ColorModel.getColor, 0)
    guideLine = new MultiLine
    multiLine = new MultiLine
  }
}