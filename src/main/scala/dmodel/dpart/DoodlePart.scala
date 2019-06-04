package dmodel.dpart

/**
  * Trait for all possible line types.

  * @author Qazhax
  */

import java.awt.geom.Path2D

import dmodel.json.JsonStroke
import dmodel.{Coord, Layer}


trait DoodlePart{
  def distFromEdge(point:Coord):Double // used to select closest line to a point, returns distance from rendered line (center of line - linesize), <0 if within the rendered line, inf is no line
  def distFromCenter(point:Coord):Double // returns distance from center of the line
  def getLines:Array[BasicLine] // used to draw the lines
  //def getPath:Array[Path2D] // testing for drawing the lines
  def transform(transformation:Coord=>Coord):Option[DoodlePart] // used for perspective transform, etc
  def length2:Double = getLines.foldLeft(0.0)(_+_.length2) //FIXME: this won't give comparable lengths, x^2+x^2 != (2x)^2
  def selection:Option[DoodlePart] // used for visualizing the selected line
  //def toJson:Option[JsonStroke]   // not used!!
  def toJsonString:Option[String] // not used!!
  def toShortJsonString:Option[String]
  def toSVGString:String // the strokes are joined with empty string and 3 empty strings is still empty string, so "" is ok if the lines are not visible
  def onUndo(layer: Layer):Boolean = {true} // used for edit lines to undo the edit, return true if successful, return false if edited line is not in he layer
  def onRedo(layer: Layer):Boolean = {true}
}
