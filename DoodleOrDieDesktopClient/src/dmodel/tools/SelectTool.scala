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
    select(dp, coord, control, alt)
  }
  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    selected = hovering
  }
  override def getLines() = {
    val buf:Buffer[DoodlePart] = Buffer()
    hovering.foreach { x => buf += x }
    selected.foreach { x => buf += x }
    buf
  }

  def select(dp:DoodlePanel, place:Coord, control:Boolean, alt:Boolean){
    val strokes = if(alt) {
      dp.model.getLayers.flatMap(_.getStrokes(false))
    }
    else dp.model.getMid.getStrokes(false)
    if(strokes.length<1)return
    var curr = strokes(0)
    var best = curr.distFrom(place)
    for(s<-strokes){
      val dist = s.distFrom(place)
      if(dist<best){
        best = dist
        curr = s
      }
    }
    if(best<20){
     hovering = Some(curr)
    } else {
      hovering = None
    }
  }
  override def cleanUp{
    hovering = None
    selected = None
  }
  
}