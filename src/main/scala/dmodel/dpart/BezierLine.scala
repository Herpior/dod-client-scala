package dmodel.dpart

import java.awt.Color

import dmodel.{Bezier, Colors, Coord, JsonStroke}


class BezierLine(var color:Color, var size:Double) extends DoodlePart {
  //var size = 1.0
  //var color = "#000"
  private val coords = Array.fill(4)(Coord(0,0))
  require (coords.length==4)
  //val xs = Array(0.0,0.0,0.0,0.0)
  //val ys = Array(0.0,0.0,0.0,0.0)
  /*var x2 = 0.0
  var y2 = 0.0
  var x3 = 0.0
  var y3 = 0.0
  var x4 = 0.0
  var y4 = 0.0*/
  def transform (transformation:Coord=>Coord):BezierLine = {
    val next = new BezierLine(this.color,this.size)
    next.setCoords(this.coords.map(c=>transformation(c)))
    next
  }
  def getCoordAt(dist:Double):Coord={
    if(dist<=0)return coords.head
    if(dist>=1)return coords.last
    Bezier.pointAt(dist,coords(0),coords(1),coords(2),coords(3))
  }
  def getCoord (ind:Int)={
    if(ind>=0&&ind<4)
      coords(ind) else coords(0)
  }
  def setCoord(ind:Int,place:Coord){
    if(ind>=0&&ind<4)
      coords(ind) = place
  }
  def setCoords(places:Array[Coord]){
    val maxi = math.min( places.length,4)
    for(i<-0 until maxi){
      coords(i) = places(i)
    }
  }
  def distFrom(point:Coord)={
    (this.coords++this.getLine(color,size).getCoords).map(_.dist(point)).sorted.head
  }
  def getLine(color1:Color,size1:Double):BasicLine={
    if(coords.forall(_==coords(0))){
      val st = new BasicLine(color1,size1)
      st.setCoords( Array(coords(0)) )
      return st
    }
    val cs = Bezier.curve(coords(0),coords(1),coords(2),coords(3))
    val res = new BasicLine(color1,size1)
    res.setCoords (cs.map { c => Coord(math.round(c.x*2)/2.0,math.round(c.y*2)/2.0) })
    res
  }
  def getLines : Array[BasicLine]= {
    val res = this.getLine(color,size)
    //res.xs = xys._1.map(x=>math.round(2*x)/2.0)
    //res.ys = xys._2.map(y=>math.round(2*y)/2.0)
    Array(res)
  }
  def selection = {
    val res = new MultiLine
    res.addLine(this.getLine(Colors.inverse(color),1))
    val line = new BasicLine(Color.black,1)
    line.setCoords(coords)
    res.addLine(line)
    res
  }
  def toJson = {
    val json = new JsonStroke
    json.color = Colors.toHexString(color)
    json.size = size
    json.coords = coords.map(_.toJson)
    json.linetype = "bezier"
    json
  }
  def toJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"linetype\":\"bezier\",\"color\":\""+Colors.toHexRGBA(color)+"\",\"size\":"+sizestr+",\"coords\":["+coords.map(_.toJsonString).mkString(",")+"]}"
  }
  def toShortJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"l\":\"b\",\"c\":\""+Colors.toHexRGBA(color)+"\",\"s\":"+sizestr+",\"p\":["+coords.map(_.toShortJsonString).mkString(",")+"]}"
  }
}