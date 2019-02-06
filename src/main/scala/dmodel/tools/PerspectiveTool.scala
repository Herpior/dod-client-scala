package dmodel.tools
import dmodel.DoodleBufferer
import dmodel.Coord
import dmodel.Magic
import dmodel.Perspective
import dmodel.dpart.{BasicLine, DoodlePart}

import collection.mutable.Buffer
import scala.collection.mutable

class PerspectiveTool extends BasicTool {
  
  private var closest : Option[Int] = None // index of the currently closest vanishing point, the perspective model should keep it in proper range
  
  override def isBusy() = true
  
  // show vanishing points as dots, 
  // lines between the dots, or a cross if less than 3 vanishing points
  // with two points, the center line defines the horizon and the other line should cross it in 90 degree angle
  override def getLines(): mutable.Buffer[DoodlePart] = {
    val vps = Perspective.getVanishingPoints
    val buf = Buffer[DoodlePart]()
    
    vps.foreach { //dots for each vanishing point
      vp => 
        val dot = new BasicLine(Magic.red, 3.0)
        dot.addCoord(vp)
        buf += dot 
    }
    
    if(vps.size == 1) { // cross at the vanishing point
      val vp = vps.head
      val hori = new BasicLine(Magic.red, 1.0)
      hori.addCoord(Coord(vp.x, vp.y-Magic.y))
      hori.addCoord(Coord(vp.x, vp.y+Magic.y))
      buf += hori 
      val vert = new BasicLine(Magic.red, 1.0)
      vert.addCoord(Coord(vp.x-Magic.x, vp.y))
      vert.addCoord(Coord(vp.x+Magic.x, vp.y))
      buf += vert 
    }
    else if(vps.size >= 2) { // connect the vanishing points
      val vp1 = vps(0)
      val vp2 = vps(1)
      val hori = new BasicLine(Magic.red, 1.0)
      val difference = vp1-vp2
      hori.addCoord(vp1 + difference)
      hori.addCoord(vp2 - difference)
      buf += hori 
      if(vps.size == 2){ // cross between the vanishing points
        val vert = new BasicLine(Magic.red, 1.0)
        val center = (vp1+vp2)/2
        val slope = difference.perpendiculated
        vert.addCoord(center - slope)
        vert.addCoord(center + slope)
        buf += vert 
      }
      else if(vps.size > 2){ // add the connections to the third vanishing point
        val vp3 = vps(2)
        val difference2 = vp1-vp3
        val difference3 = vp2-vp3
        val line2 = new BasicLine(Magic.red, 1.0)
        line2.addCoord(vp1)
        line2.addCoord(vp3-difference2)
        buf += line2 
        val line3 = new BasicLine(Magic.red, 1.0)
        line3.addCoord(vp2)
        line3.addCoord(vp3-difference3)
        buf += line3 
      }
    }
    
    closest.foreach { // dot to show you can drag the vanishing point without using control or alt
      vpi => 
        val dot = new BasicLine(Magic.white, 1.5)
        dot.addCoord(vps(vpi-1))
        buf += dot 
    }
    buf
  }
  
  
  
  //if near a vanishing point, grab vanishing point
  //else check if ctrl or alt pressed and decide which vanishing point to grab from that
  //if left click, deleting vanishing point instead
  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(button == 1) setVanishingPoint(coord, control, alt)
    else if(button == 3) deleteVanishingPoint(coord, control, alt)
    db.redrawDrawing
    //db.repaint
  }
  override def onMouseUp  (db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    //stop dragging vanishing point
    if(button == 1) setVanishingPoint(coord, control, alt)
    checkClosest(coord)
    db.redrawDrawing
    //db.repaint
  }
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    //move vanishing point
    if(left) setVanishingPoint(coord, control, alt)
    db.redrawDrawing
    //db.repaint
  }
  override def onMouseMove(db:DoodleBufferer, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {
    //if near vanishing point add a white dot on the closest one, if not, remove the
    if(checkClosest(coord)) {
      db.redrawDrawing
      //db.repaint
    }
  }
  
  def setVanishingPoint(coord:Coord, control:Boolean, alt:Boolean) {
    val ind = if(closest.nonEmpty) closest.get else if (control) 2 else if (alt) 3 else 1
    closest = Some(Perspective.setVanishingPoint(ind, coord))
  }
  def deleteVanishingPoint(coord:Coord, control:Boolean, alt:Boolean) {
    val ind = if(closest.nonEmpty) closest.get else if (control) 2 else if (alt) 3 else 1
    Perspective.removeVanishingPoint(ind)
    checkClosest(coord)
  }
  // checks which vanishing point, if any, is closer than 50 in-image pixels to the cursor
  // returns true if the tool graphics should be redrawn
  def checkClosest(coord:Coord): Boolean = {
    val vps = Perspective.getVanishingPoints
    val dists = vps.map { vp => vp.dist(coord) }
    val distsWind = dists.zipWithIndex
    val close = distsWind.minBy(x => x._1)
    val prev = closest
    if(close._1 < 50) closest = Some(close._2 +1)
    else closest = None
    
    if(closest != prev) true
    else false
  }
  
  //---------\\
  /* old code from dmodel lol
  def startPerspective(place:Coord,mods:Int){
  }
  def dragPerspective(place:Coord,mods:Int){
    if(mods/128%2==1)Perspective.setTertiary(place)
    else if(mods/512%2==1)Perspective.setSecondary(place)
    else Perspective.setPrimary(place)
  }
  def removePerspective(mods:Int){
    if(mods/128%2==1)Perspective.removeTertiary
    else if(mods/512%2==1)Perspective.removeSecondary
    else Perspective.removePrimary
  }*/
  
}