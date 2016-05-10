package dmodel

import math.Pi
import java.awt.Color

object LineTool {

  def startLine(next:MultiLine,color:Color,size:Int,place:Coord,mods:Int){
        //if(place.x>=0 && place.x <= Magic.x && place.y >= 0 && place.y <= Magic.y){
          next.addLine(new BasicLine(color,size){
            this.addCoord(place)
          })
        //} else {
        //  next.addLine(new BasicLine(color,size))
        //}
  }
  /*def addLine(e:MouseEvent){
    
  }*/
  def dragLine(next:BasicLine,place:Coord,mods:Int){
    //var x = getX(e)
    //var y = getY(e)
      val last = next.getCoords
      val len = last.length
      if(len<=1){
        //if(place.x>=0 && place.x <= Magic.x && place.y >= 0 && place.y <= Magic.y){
          next.addCoord(place)
          //last.xs+=x
          //last.ys+=y
        //}
      } else {
        if(mods/128%2==1){
          val c0 = last(len-2)
          val dc = place-c0
          //println(place+" - "+c0+" = "+dc)
          val dlen = math.sqrt(dc.x*dc.x+dc.y*dc.y)//place.dist(c0)
          //println(dlen+" <- "+place.dist(c0))
          val xy = 
              if (mods/64%2==1) Perspective.getCoord(c0,Angle.angle(dc.x,dc.y),dlen)
              else Angle.getCoord(math.round(Angle.angle(dc.x,dc.y)/Pi*4)*Pi/4,dlen)
              //println("coord: "+xy+" mods ="+mods)
          val c2 = c0+xy
          //if(c2>=Coord(0)&&c2<=Magic.doodleSize)
          next.setLast(c2)
        } else {
          //if(place.x>=0 && place.x <= Magic.x && place.y >= 0 && place.y <= Magic.y){
            next.setLast(place)
          //}
        }
        //last.xs(len-1) = x
        //last.ys(len-1) = y
      }
      //next.change(side.bcolor, side.bsize, last.xs.last, last.ys.last, x/zoom, y/zoom)
      //restroke
    
  }
  /*
  def addLinePoint(next:MultiLine){
    if(next.getLines.length>0){
      val st = next.getLines.last
      st.addCoord(st.getCoords.last)
    }
  }*/
  def addLine(next:MultiLine,color:Color,size:Int,place:Coord,mods:Int){
    //val x = e.point.getX+(side.offsetX*zoom)
    //val y = e.point.getY+(side.offsetY*zoom)
    //if(place.x>=0 && place.x<= Magic.x && place.y >= 0 && place.y <= Magic.y){
      if(next.getLines.isEmpty){
        next.addLine(new BasicLine(color,size){
          this.addCoord(place)
        })
      } else {
        val last = next.getLines.last
        if(last.color == color && last.size == size){
          last.addCoord(place)
        }
        else{
          next.addLine(new BasicLine(color,size){
            last.getLastOption.foreach(c=>this.addCoord(c))
            this.addCoord(place)
          })
        }
      }
    /*}else{ 
      if(next.getLast.exists { x => !x.getCoords.isEmpty }){
        
        if(mods/128%2==1){
          val last = next.getLines.last
          next.addLine(new BasicLine(color,size){
          last.getLastOption.foreach(c=>this.addCoord(c))})
        }
        else next.addLine(new BasicLine(color,size))
      }
    }*/
  }
}