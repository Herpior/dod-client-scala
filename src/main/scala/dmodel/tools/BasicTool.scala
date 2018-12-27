package dmodel.tools
import dmodel.{Coord, DoodleBufferer}
import dmodel.dpart.DoodlePart

import collection.mutable.Buffer

trait BasicTool {
  
  def isBusy():Boolean = {false}
  def initTool() {}
  def cleanUp() {}
  def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseUp  (db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseMove(db:DoodleBufferer, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {}
  def getLines():Buffer[DoodlePart] = {collection.mutable.Buffer()} //for drawing the stroke or informational graphics, not for adding strokes to layer
  def getLastLine():Option[DoodlePart] = {None} // for drawing one segment of the stroke
  // for configuring from the ui
  def getConfigVariables():Vector[ConfigVariable] = Vector()

}