package dmodel.dpart

/**
  * Json class for gson to load doodles from the server.
  * A single line from a doodle.
  *
  * @author Qazhax
  */

import dmodel.{Colors, Coord, JsonStroke}

import scala.collection.mutable.Buffer


class JsonLine extends DoodlePart {

  var color:String = _
  var size:Double = _
  var path:Array[Double]= Array()

  def transform (transformation:Coord=>Coord): Some[BasicLine] = {
    val next = this.toBasicLine
    next.transform(transformation)
  }
  def distFrom(point:Coord): Double ={
    this.toBasicLine.distFrom(point)
  }
  def getLines: Array[BasicLine] = {
    Array(this.toBasicLine)
  }
  def toDoodlePart: BasicLine = toBasicLine
  def toBasicLine: BasicLine ={
    val res = new BasicLine(Colors.toColor(this.color),this.size)
    val buf = Buffer[Coord]()
    for(i<-0 until this.path.length/2){
      buf += Coord(this.path(i*2),this.path(i*2+1))
    }
    res.setCoords(buf)
    res
  }
  def selection: Some[BasicLine] = {
    this.toBasicLine.selection
  }
  def toJson: Some[JsonStroke] = {
    val res = new JsonStroke
    res.color = color
    res.linetype = "basic"
    res.path = path
    res.size = size
    Some(res)
  }
  def toJsonString: Some[String] = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"linetype\":\"json\",\"color\":"+color+",\"size\":"+sizestr+",\"path\":["+path.mkString(",")+"]}")
  }
  def toShortJsonString: Some[String] = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"l\":\"j\",\"c\":"+color+",\"s\":"+sizestr+",\"p\":["+path.mkString(",")+"]}")
  }
}