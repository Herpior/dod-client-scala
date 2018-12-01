package dmodel.tools

import collection.mutable.Buffer
import dmodel.Coord
import dmodel.dpart.DoodlePart
import view.DoodlePanel


//base for tools that need to select lines
class SelectTool extends BasicTool {
  
  protected var selected:Array[DoodlePart] = Array() // selected line after clicking
  protected var hovering:Array[DoodlePart] = Array() // line for visualization
  
  override def onMouseMove(dp:DoodlePanel, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    if(selectOne(dp, coord, control, alt)){
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    selected = hovering
  }
  override def getLines() = {
    val buf:Buffer[DoodlePart] = Buffer()
    hovering.foreach { _.selection.foreach(buf += _) }
    selected.foreach { _.selection.foreach(buf += _) }
    buf
  }
  
  override def cleanUp{
    unselect
  }

  // returns boolean that tells whether there has been a change and the graphics should be redrawn
  def selectOne(dp:DoodlePanel, place:Coord, control:Boolean, alt:Boolean):Boolean={
    val strokes = if(alt) {
      dp.model.getLayers.flatMap(_.getStrokes(false))
    }
    else dp.model.getMid.getStrokes(false)
    if(strokes.length<1) return false
    var curr = strokes(0)
    var best = curr.distFrom(place)
    for(s<-strokes){
      val dist = s.distFrom(place)
      if(dist<best){
        best = dist
        curr = s
      }
    }
    val prev = hovering
    if(best<20){
     hovering = Array(curr)
    } else {
      hovering = Array()
    }
    hovering != prev
  }
  def unselect {
    hovering = Array()
    selected = Array()
  }
  
  def allSelected = (hovering ++ selected).toArray
  
  
}