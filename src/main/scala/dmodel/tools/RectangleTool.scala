package dmodel.tools

/**
  * A tool that can be used to draw straight lines

  * @author Qazhax
  */

import math.Pi
import java.awt.Color

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

class RectangleTool extends LineTool {

  private var start:Option[Coord] = None
  private var roundedOutlines = false
  private var fill = true
  private var macaronBugMode = true

  override def getConfigVariables: Vector[ConfigVariable] = {
    val fillConfig = new BooleanConfigVariable("use fill", _=>this.fill, fill=_)
    val roundedConfig = new BooleanConfigVariable("rounded corners", _=>this.roundedOutlines, roundedOutlines=_)
    Vector(fillConfig, roundedConfig).asInstanceOf[Vector[ConfigVariable]]
  }

  override def isBusy: Boolean = start.isDefined

  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean){
    if(button == 1){
      dragRect(coord, control, shift)
      db.redrawDrawing
      //db.repaint
    }
  }

  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      dragRect(coord, control, shift)
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseUp(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean){
    if(button == 1){
      stopLine(coord, db.model)
      start = None
      db.redrawDrawing
      db.redrawLastMid
      //db.repaint
    }
  }


  /*def addLine(e:MouseEvent){

  }*/
  def dragRect(place:Coord, control:Boolean, shift:Boolean){
    val outlineColor = ColorModel.getColor
    val fillColor = ColorModel.getColor2
    val scaleFactor = 3

    // clear all old lines, no use trying to edit all of them when the size of the rectangle
    // can vary e.g. from 1px to 400px in one frame anyways
    multiLine.setLines(Array[BasicLine]())

    // make sure start exists
    if(start.isEmpty) start = Some(place)
    val startP = start.get

    // use square if ctrl is on
    val cornerDelta = place - startP
    val endP = if(control) useRuler(startP, place, shift) else place

    // rotate the values around so that startLine has both smaller values and endline has both larger values
    val (startPoint, endPoint) = startP.reassembleAABBCorners(endP)

    // count how many lines of what sizes are needed to fill the area
    val maxFillSize = if(macaronBugMode) cornerDelta.abs.min.toInt else (endPoint-startPoint).min.toInt// can't use brush that is larger than the rectangle
    val outlineSize = math.min(SizeModel.getSize,maxFillSize)
    val needsFill = maxFillSize >= outlineSize * 2 // no need to fill if outlines are larger than it
    val minFillSize = math.min(outlineSize * scaleFactor, maxFillSize) // minimum size that the fill needs to hide corners behind outlines

    // add lines starting from the largest
    var size = maxFillSize
    if(needsFill && fill){
      var done = false
      while (!done){
        multiLine.addLine(getRectLine(startPoint, endPoint, size, fillColor))
        if(size <= minFillSize) done = true
        size = (size *1.0 / scaleFactor).ceil.toInt
      }
    }
    size = outlineSize
    if(roundedOutlines){
      multiLine.addLine(getRectLine(startPoint, endPoint, size, outlineColor, true))
    }
    else {
      size = (size*0.9).ceil.toInt
      while (size >= 2){
        multiLine.addLine(getRectLine(startPoint, endPoint, size, outlineColor))
        size = (size *1.0 / scaleFactor).ceil.toInt
      }
      multiLine.addLine(getRectLine(startPoint, endPoint, 1, outlineColor, true))
    }
  }

  // creates rectangle inside the area between start and end points
  // both x and y in startpoint are smaller than x and y in endpoint
  def getRectLine(startPoint:Coord, endPoint:Coord, size:Double, color: Color, lastLine:Boolean=false): BasicLine ={
    val extraSpace = if(!lastLine) (math.max(size*0.1, 0.5)*2).floor*0.5 else 0
    var lineStart = startPoint + Coord(size*0.5+extraSpace)
    var lineEnd = endPoint - Coord(size*0.5+extraSpace)
    val pointDelta = endPoint - startPoint
    // check if the line coordinates flip
    val xOverride = pointDelta.x - (size+extraSpace*2) <= 0
    val yOverride = pointDelta.y - (size+extraSpace*2) <= 0
    // use the midpoint instead if they
    val center = (startPoint + endPoint) * 0.5
    if(xOverride) {
      lineStart = Coord(center.x, lineStart.y)
      lineEnd = Coord(center.x, lineEnd.y)
    }
    if(yOverride) {
      lineStart = Coord(lineStart.x, center.y)
      lineEnd = Coord(lineEnd.x, center.y)
    }
    // create the rect line
    val line = new BasicLine(color,size)
    line.addCoord(lineStart)
    if(xOverride != yOverride){
      line.addCoord(lineEnd)
    }
    else if(!xOverride && !yOverride) {
      line.addCoord(Coord(lineEnd.x, lineStart.y))
      line.addCoord(lineEnd)
      line.addCoord(Coord(lineStart.x, lineEnd.y))
      line.addCoord(lineStart)
    }
    line
  }


}