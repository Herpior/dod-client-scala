package dmodel.tools

/**
  * A tool that can be used to select lines.
  * Base for tools that need to select lines, useless on it's own.

  * @author Qazhax
  */

import collection.mutable.Buffer
import dmodel.{Coord, DoodleBufferer}
import dmodel.dpart.DoodlePart

import scala.collection.mutable


class SelectTool extends BasicTool {
  
  protected var selected:Option[DoodlePart] = None//Array() // selected line after clicking
  protected var hovering:Option[DoodlePart] = None//Array() // line for visualization
  
  override def onMouseMove(db:DoodleBufferer, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    if(selectOne(db, coord, control, alt)){
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    selected = hovering
  }
  override def getLines(): mutable.Buffer[DoodlePart] = {
    val buf:Buffer[DoodlePart] = Buffer()
    hovering.foreach { _.selection.foreach(buf += _) }
    selected.foreach { _.selection.foreach(buf += _) }
    buf
  }
  
  override def cleanUp(){
    unselect
  }

  // returns boolean that tells whether there has been a change and the graphics should be redrawn
  def selectOne(db:DoodleBufferer, place:Coord, control:Boolean, alt:Boolean):Boolean={
    val strokes = db.model.getMid.getVisibleStrokes(false)
    /*if(alt) {
      db.model.getLayers.flatMap(_.getVisibleStrokes(false)) //maybe think about selectTool that can select lines from any layer
    }
    else*/
    if(strokes.length<1) return false
    var currentbest = strokes.last
    var best = Double.MaxValue//currentbest.distFromEdge(place)
    for(s<-strokes.reverseIterator){
      val dist = s.distFromEdge(place)
      if(dist<best){
        best = dist
        currentbest = s
        if(best<0){
          hovering = Some(currentbest)
          return true
        }
      }
    }
    val prev = hovering
    if(best<20){
     hovering = Some(currentbest)
    } else {
      hovering = None
    }
    hovering != prev
  }
  def unselect() {
    hovering = None
    selected = None
  }
  
  def allSelected: Array[DoodlePart] = (hovering ++ selected).toArray
  
  
}