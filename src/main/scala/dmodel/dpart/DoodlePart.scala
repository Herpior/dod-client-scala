package dmodel.dpart

import dmodel.{Coord, JsonStroke, Layer}


trait DoodlePart{
  def distFrom(point:Coord):Double
  def getLines:Array[BasicLine]
  def transform(transformation:Coord=>Coord):DoodlePart
  def length2:Double = getLines.foldLeft(0.0)(_+_.length2)
  def selection:DoodlePart
  def toJson:JsonStroke
  def toJsonString:String
  def toShortJsonString:String
  def onUndo(layer: Layer){}//used for edit lines to undo the edit
  def onRedo(layer: Layer){}
}
