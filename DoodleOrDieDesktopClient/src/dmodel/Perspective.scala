package dmodel

import collection.mutable.Buffer
import math.{Pi,abs}

object Perspective {
  private var pers = Magic.doodleSize/2
  private var pers2 :Option[Coord] = None
  private var pers3 :Option[Coord] = None 
  
  def setPrimary(coord:Coord){
    pers = coord
  }
  def setSecondary(coord:Coord){
    pers2 = Some(coord)
  }
  def setTertiary(coord:Coord){
    pers3 = Some(coord)
  }
  
  def removePrimary{
    pers = Magic.doodleSize/2
  }
  def removeSecondary{
    pers2 = None
  }
  def removeTertiary{
    pers3 = None
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
    } else math.Pi/2
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