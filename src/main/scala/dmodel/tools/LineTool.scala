package dmodel.tools

/**
  * A tool that can be used to draw straight lines

  * @author Qazhax
  */

import math.Pi
import java.awt.Color

import dmodel.Colors
import dmodel.dpart.DoodlePart

import scala.collection.mutable

//import dmodel.Angle
import dmodel.Coord
import dmodel.Perspective
import dmodel.ColorModel
import dmodel.SizeModel
import dmodel.dpart.{BasicLine, MultiLine}
import dmodel.DoodleBufferer

//object LineTool extends LineToolClass

class LineTool extends BasicTool {
  
  private var drawing = false
  protected var transparency = 0.0
  
  override def isBusy(): Boolean = drawing
  
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      dragLine(coord, control, shift)
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseUp(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean){
    if(button == 1){
      stopLine(coord, db.model)
      db.redrawDrawing
      db.redrawLastMid
      //db.repaint
    }
  }
  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean){
    if(button == 1){
      startLine(coord)
      db.redrawDrawing
      //db.repaint
    }
    else if(button == 3 && isBusy){
      addLine(getColor, SizeModel.getSize, coord)
      //doodle.model.dragLine(place,mods)
      db.redrawDrawing
      //db.repaint
    }
  }
  override def getLines(): mutable.Buffer[DoodlePart] = {
    multiLine.getLines.toBuffer
  }
  override def getLastLine(): Option[DoodlePart] = {
    multiLine.getLast.flatMap (_.getLastLine)
  } //  MultiLine.getLast.flatMap()
  
  protected var multiLine:MultiLine = new MultiLine

   
  def getLast: Option[BasicLine] = {
    //multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
    multiLine.getLast.flatMap(_.getLastLine)
  }
  
  def startLine(color:Color,size:Int,place:Coord){
    this.drawing = true
    multiLine.addLine(new BasicLine(color,size){
      this.addCoord(place)
    })
  }
  
  def startLine(place:Coord){
    startLine(getColor, SizeModel.getSize, place)
  }
  /*def addLine(e:MouseEvent){
    
  }*/
  def dragLine(place:Coord, control:Boolean, shift:Boolean){
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
        if(control){
          //if(c2>=Coord(0)&&c2<=Magic.doodleSize)
          val c0 = last(len-2)
          val c2 = useRuler(c0, place, shift)
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

  def getColor ={
    Colors.reduceOpacity(ColorModel.getColor, transparency)
  }

  // uses either 8 directional ruler or perspective ruler if perspectiveRuler is true
  def useRuler(startPoint:Coord, endPoint:Coord, perspectiveRuler:Boolean)={
    val dc = endPoint-startPoint
    //println(place+" - "+c0+" = "+dc)
    val dlen = math.sqrt(dc.x*dc.x+dc.y*dc.y)//place.dist(c0)
    //println(dlen+" <- "+place.dist(c0))
    val xy =
    if (perspectiveRuler) Perspective.getDisplacement(startPoint, endPoint)//Angle.angle(dc.x,dc.y),dlen)
    else Coord.fromAngle(math.round(dc.toAngle/Pi*4)*Pi/4,dlen)
    //println("coord: "+xy+" mods ="+mods)
    startPoint+xy
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
    this.drawing = false
    multiLine.compress
       // this.layers.getCurrent.add(multiLine)
    
    model.layers.getCurrent.add(multiLine)
    multiLine = new MultiLine
  }


  override def getConfigVariables(): Vector[ConfigVariable] = {
    val transparencyConfig = new DoubleConfigVariable("transparency", _=>transparency, transparency=_, Some(0.0), Some(1.0), false)
    Vector(transparencyConfig)//.asInstanceOf[Vector[ConfigVariable]]
  }

}