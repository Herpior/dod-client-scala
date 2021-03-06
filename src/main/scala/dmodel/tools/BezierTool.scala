package dmodel.tools

/**
  * A tool that can be used to draw bezier lines consisting of start and end points, and two guide points.

  * @author Qazhax
  */

//import dmodel.Angle
import dmodel._
import dmodel.dpart.{BasicLine, BezierLine, DoodlePart, MultiLine}

import math.Pi
import collection.mutable.Buffer
import dmodel.DoodleBufferer

class BezierTool extends LineTool {
  
  
  // state 0 = nothing ongoing
  // state 1 = mouse down first time, act like drawing straight line
  // state 2 = mouse up firsts time, line follows on move
  // state 3 = mouse down second time, moving the last
  private var state = 0
  private var bezierLine: BezierLine = new BezierLine(ColorModel.getColor,SizeModel.getSize)
  private var guideLine: MultiLine = new MultiLine
  
  
  override def onMouseMove(db:DoodleBufferer, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    if(state == 2){
      dragBezier(1, coord, control, shift)
      dragBezier(2, coord, control, shift)
      db.redrawDrawing
    }
  }
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    if(left){
      if(state == 1){
        dragBezier(3, coord, control, shift)
      } else {
        dragBezier(2, coord, control, shift)
      }
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseUp(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(button == 1){
      if(state == 1){
        dragBezier(3, coord, control, shift)
        state = 2
      } else {
        dragBezier(2, coord, control, shift)
        stopBezier(db.model)
        db.redrawLastMid
        state = 0
      }
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(button == 1){
      if(state == 0){
      //startLine(e)
      startBezier(coord)
      state = 1
      }
      else{
        dragBezier(1, coord, control, shift)
        state = 3
      }
      db.redrawDrawing
      //db.repaint
    }
  }
  override def getLines() : Buffer[DoodlePart]= {
    if(state == 0) return Buffer()
    val buf = bezierLine.getLines ++ guideLine.getLines
    buf.toBuffer
  } //for redrawing the whole line while drawing?
  
  //def initTool() {}
  //def onMouseUp(db:DoodleBufferer, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseMove(db:DoodleBufferer, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  
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
                if (shift) Perspective.getDisplacement(c0,place) //Angle.angle(dc.x,dc.y),len)
                else Coord.fromAngle(math.round(dc.toAngle/Pi*4)*Pi/4, len) //TODO: make perspective do the heavy lifting in 8-direction ruler too
              coord = c0+xy
            case 2 =>
              val c0 = bezierLine.getCoord(3)
              val dc = place-c0
              val len = place.dist(c0)
              val xy =
                if (shift) Perspective.getDisplacement(c0,place)//Angle.angle(dc.x,dc.y),len)
                else Coord.fromAngle(math.round(dc.toAngle/Pi*4)*Pi/4,len)
              coord = c0+xy
            case _ =>
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