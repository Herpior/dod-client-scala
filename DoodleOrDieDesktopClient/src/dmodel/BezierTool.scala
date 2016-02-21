package dmodel

import math.Pi

object BezierTool {

  /*def inverseColor(color:String){
    if(color.length==7){
      val r = color(1)+""+color(2)
      val g = color(3)+""+color(4)
      val b = color(5)+""+color(6)
      
    }
  }*/
  
  def startBezier(next:BezierLine,guide:MultiLine,place:Coord,mods:Int){
    val inverse = Colors.inverse(next.color)
    guide.addLine( new BasicLine(inverse,0.1) )
    val s = guide.getLines.head
    for(i <- 0 to 3){
      next.setCoord(i,place)
      s.addCoord(place)
    }
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
              println("you dun fucked up dragbezier pt: "+pt)
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