package dmodel.tools

import math.Pi
import java.awt.Color
import dmodel.Angle
import dmodel.BasicLine
import dmodel.Coord
import dmodel.MultiLine
import dmodel.Perspective
import dmodel.ColorModel
import dmodel.SizeModel
import view.DoodlePanel

object LineTool extends LineToolClass

class LineToolClass extends BasicTool {
  
  override def onMouseUp(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    stopLine(coord, dp.model)
    dp.redrawDrawing
    dp.redrawLastMid
    dp.repaint
  }
  override def onMouseDown(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      startLine(coord)
      dp.redrawDrawing
      dp.repaint
    }
    else if(right){
      addLine(ColorModel.getColor, SizeModel.getSize, coord)
      //doodle.model.dragLine(place,mods)
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def getLines() = {
    multiLine.getLines.toBuffer
  }
  override def getLastLine() = {
    multiLine.getLast.flatMap (_.getLastLine)
  } //  MultiLine.getLast.flatMap()
  
  protected var multiLine:MultiLine = new MultiLine

  def startLine(color:Color,size:Int,place:Coord){
        //if(place.x>=0 && place.x <= Magic.x && place.y >= 0 && place.y <= Magic.y){
          multiLine.addLine(new BasicLine(color,size){
            this.addCoord(place)
          })
        //} else {
        //  next.addLine(new BasicLine(color,size))
        //}
  }
  
  def startLine(place:Coord){
    startLine(ColorModel.getColor, SizeModel.getSize, place)
  }
  /*def addLine(e:MouseEvent){
    
  }*/
  def dragLine(place:Coord,mods:Int){
    //var x = getX(e)
    multiLine.getLast.foreach{ next =>
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
  }
  /*
  def addLinePoint(next:MultiLine){
    if(next.getLines.length>0){
      val st = next.getLines.last
      st.addCoord(st.getCoords.last)
    }
  }*/
  def addLine(color:Color,size:Int,place:Coord){
    //val x = e.point.getX+(side.offsetX*zoom)
    //val y = e.point.getY+(side.offsetY*zoom)
    //if(place.x>=0 && place.x<= Magic.x && place.y >= 0 && place.y <= Magic.y){
      if(multiLine.getLines.isEmpty){
        multiLine.addLine(new BasicLine(color,size){
          this.addCoord(place)
        })
      } else {
        val last = multiLine.getLines.last
        if(last.color == color && last.size == size){
          last.addCoord(place)
        }
        else{
          multiLine.addLine(new BasicLine(color,size){
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
  
  def stopLine(place:Coord, model:dmodel.DoodleModel) {
    multiLine.compress
       // this.layers.getCurrent.add(multiLine)
    
    model.layers.getCurrent.add(multiLine)
    multiLine = new MultiLine
  }
}