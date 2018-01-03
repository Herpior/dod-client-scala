package dmodel.tools
import dmodel.Angle
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

object BezierTool extends BasicTool {
  
  // state 0 = nothing ongoing
  // state 1 = mouse down first time, act like drawing straight line
  // state 2 = mouse up firsts time, line follows on move
  // state 3 = mouse down second time, moving the last
  private var state = 0
  private var bezierLine: Option[BezierLine] = None
  private var guideLine: Option[MultiLine] = None
  
  //def initTool() {}
  override def onMouseDown(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    val model = dp.model
  }
  //def onMouseUp(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  //def onMouseMove(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  
  //def setBezier{
  //  this.bezier = true
  //}
  def dragBezier(point:Int,place:Coord,mods:Int){
    bezierLine.foreach{next =>
      guideLine.foreach{guide =>
        BezierTool.dragBezier(point, next, guide, place, mods)
        }
      }
  }
  def stopBezier(model:DoodleModel){
    //this.bezier = false
    bezierLine.foreach(next=>model.layers.getCurrent.add(next))
    bezierLine = None
    guideLine = None
  }

  
  def startBezier(place:Coord,mods:Int){
    val bez = new BezierLine(ColorModel.getColor,SizeModel.getSize)
    val guide = new MultiLine
    val inverse = Colors.inverse(bez.color)
    guide.addLine( new BasicLine(inverse,0.1) )
    val s = guide.getLines.head
    for(i <- 0 to 3){
      bez.setCoord(i,place)
      s.addCoord(place)
    }
    bezierLine = Some(bez)
    guideLine = Some(guide)
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
  def dragBezier(pt:Int,next:BezierLine,guide:MultiLine,place:Coord,mods:Int){
    require(pt>=0 && pt <= 3)
    var coord = place
      if(mods/128%2==1){
          pt match{
            case 0 =>
            case 1 | 3 =>
              val c0 = next.getCoord(0)
              val dc = place-c0
              val len = place.dist(c0)
              val xy = 
                if (mods/64%2==1) Perspective.getCoord(c0,Angle.angle(dc.x,dc.y),len)
                else Angle.getCoord(math.round(Angle.angle(dc.x,dc.y)/Pi*4)*Pi/4,len)
              coord = c0+xy
            case 2 =>
              val c0 = next.getCoord(3)
              val dc = place-c0
              val len = place.dist(c0)
              val xy = 
                if (mods/64%2==1) Perspective.getCoord(c0,Angle.angle(dc.x,dc.y),len)
                else Angle.getCoord(math.round(Angle.angle(dc.x,dc.y)/Pi*4)*Pi/4,len)
              coord = c0+xy
            case _ =>
              //println("you dun fucked up dragbezier pt: "+pt)
          }
        }
        next.setCoord(pt, coord)
        val s = guide.getLines.head
        if(pt==3){
          next.setCoord(2,coord)
          s.setCoord(2,coord)
        }
        s.setCoord(pt,coord)
          
  }
  
}