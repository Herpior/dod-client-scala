package dmodel.dpart

import dmodel.{Colors, Coord, JsonStroke}

import scala.collection.mutable.Buffer


class JsonLine extends DoodlePart {

  var color:String = _
  var size:Double = _
  var path:Array[Double]= Array()

  def transform (transformation:Coord=>Coord):BasicLine = {
    val next = this.toBasicLine
    next.transform(transformation)
  }
  def distFrom(point:Coord)={
    this.toBasicLine.distFrom(point)
  }
  def getLines = {
    Array(this.toBasicLine)
  }
  def toDoodlePart = toBasicLine
  def toBasicLine={
    val res = new BasicLine(Colors.toColor(this.color),this.size)
    val buf = Buffer[Coord]()
    for(i<-0 until this.path.length/2){
      buf += Coord(this.path(i*2),this.path(i*2+1))
    }
    res.setCoords(buf)
    res
  }
  def selection = {
    this.toBasicLine.selection
  }
  def toJson = {
    val res = new JsonStroke
    res.color = color
    res.linetype = "basic"
    res.path = path
    res.size = size
    res
  }
  def toJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"linetype\":\"json\",\"color\":"+color+",\"size\":"+sizestr+",\"path\":["+path.mkString(",")+"]}"
  }
  def toShortJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"l\":\"j\",\"c\":"+color+",\"s\":"+sizestr+",\"p\":["+path.mkString(",")+"]}"
  }
}