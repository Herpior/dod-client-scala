package dmodel.dpart

import dmodel.{Coord, JsonStroke, Layer}


trait DoodlePart{
  def distFrom(point:Coord):Double // used to select closest line to a point
  def getLines:Array[BasicLine] // used to draw the lines
  def transform(transformation:Coord=>Coord):Option[DoodlePart] // used for perspective transform, etc
  def length2:Double = getLines.foldLeft(0.0)(_+_.length2) //FIXME: this won't give comparable lengths, x^2+x^2 != (2x)^2
  def selection:Option[DoodlePart] // used for visualizing the selected line
  def toJson:Option[JsonStroke]   // not used!!
  def toJsonString:Option[String] // not used!!
  def toShortJsonString:Option[String]
  def onUndo(layer: Layer):Boolean = {true} // used for edit lines to undo the edit, return true if successful, return false if edited line is not in he layer
  def onRedo(layer: Layer):Boolean = {true}
}
