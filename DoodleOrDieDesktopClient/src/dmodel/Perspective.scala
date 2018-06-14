package dmodel

import collection.mutable.Buffer
import math.{Pi,abs}

object Perspective {
  private var pers = Magic.doodleSize/2
  private var pers2 :Option[Coord] = None
  private var pers3 :Option[Coord] = None 
  private var defaultAng2 = 0.0;
  private var defaultAng3 = math.Pi/2;
  
  def setPrimary(coord:Coord)={
    pers = coord
    1
  }
  def setSecondary(coord:Coord)={
    pers2 = Some(coord)
    2
  }
  def setTertiary(coord:Coord)={
    if(pers2.nonEmpty) {
      pers3 = Some(coord)
      3
    }
    else setSecondary(coord)
  }
  def setVanishingPoint(index:Int, coord:Coord)={
    if(index == 3) setTertiary(coord)
    else if(index == 2) setSecondary(coord)
    else setPrimary(coord)
  }
  
  def getVanishingPoints = {
    val buf = Buffer(pers)
    pers2.foreach { x => buf += x }
    pers3.foreach { x => buf += x }
    buf
  }
  
  def removePrimary{
    if(pers2.nonEmpty){
      pers = pers2.get
      removeSecondary // this will propagate the change to the tertiary vp if one exists
    }
    else pers = Magic.doodleSize/2 //uhh I can't delete the last vp so I'll just reset it to the center, maybe it's okay?
  }
  def removeSecondary{
    if(pers3.nonEmpty){
      pers2 = pers3
      removeTertiary
    }
    else pers2 = None
  }
  def removeTertiary{
    pers3 = None
  }
  def removeVanishingPoint(index:Int)={
    if(index == 3) removeTertiary
    else if(index == 2) removeSecondary
    else removePrimary
  }
  
  def getCoord(orig:Coord,angle:Double,length:Double)={
    val ang = Angle.angle(orig.x-pers.x, orig.y-pers.y)
    val ang2 = if(!pers2.isEmpty){
      val p = pers2.get
      Angle.angle(orig.x-p.x,orig.y-p.y)
    } else 0.0
    val ang3 = if(!pers3.isEmpty){
      val p = pers3.get
      Angle.angle(orig.x-p.x,orig.y-p.y)
    } else if(!pers2.isEmpty) {
      val difference = pers - pers2.get
      val slope = difference.perpendiculate
      slope.toAngle
    }
    else math.Pi/2
    val angs = Array[Double](ang,ang2,ang3,Pi+ang,Pi+ang2,Pi+ang3)
    val buf = Buffer[Double]()
    //println("ang: "+ang/Pi*180)
    //println("ang2: "+ang2/Pi*180)
    //println("ang3: "+ang3/Pi*180)
    for(i<- 0 until 6){
      var d = abs(angs(i)-angle)
      while (d>Pi) d = abs(d-2*Pi)
      //println(d+"<-"+angs(i))
      buf += d
    }
    val resultante = buf.zipWithIndex
    Angle.getCoord(angs(resultante.sortBy(f=>f._1).head._2), length)
  }
}