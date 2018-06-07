package dmodel.tools

import collection.mutable.Buffer
import dmodel.Coord
import dmodel.DoodlePart
import view.DoodlePanel

object SelectTool extends SelectToolClass

class SelectToolClass extends BasicTool {
  
  protected var selected:Option[DoodlePart] = None // selected line after clicking
  protected var hovering:Option[DoodlePart] = None // line for visualization
  
  override def onMouseMove(dp:DoodlePanel, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    if(select(dp, coord, control, alt)){
      dp.redrawDrawing
      dp.repaint
    }
  }
  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    selected = hovering
  }
  override def getLines() = {
    val buf:Buffer[DoodlePart] = Buffer()
    hovering.foreach { x => buf += x.selection }
    selected.foreach { x => buf += x.selection }
    buf
  }
  
  override def cleanUp{
    hovering = None
    selected = None
  }

  // returns boolean that tells whether there has been a change and the graphics should be redrawn
  def select(dp:DoodlePanel, place:Coord, control:Boolean, alt:Boolean):Boolean={
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
     hovering = Some(curr)
    } else {
      hovering = None
    }
    hovering != prev
  }
  
  def allSelected = (hovering ++ selected).toArray
  
  
}