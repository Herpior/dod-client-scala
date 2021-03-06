package dmodel.dpart

/**
  * A class representing a single bezier curve with colour and size.

  * @author Qazhax
  */

import java.awt.Color

import dmodel._
import dmodel.json.JsonStroke


class BezierLine(var color:Color, var size:Double) extends DoodlePart {
  //var size = 1.0
  //var color = "#000"
  private val coords = Array.fill(4)(Coord(0,0))
  private var basicline:BasicLine = _
  private var changed = true // true when basicline needs to be recomputed
  require (coords.length==4)
  //val xs = Array(0.0,0.0,0.0,0.0)
  //val ys = Array(0.0,0.0,0.0,0.0)
  /*var x2 = 0.0
  var y2 = 0.0
  var x3 = 0.0
  var y3 = 0.0
  var x4 = 0.0
  var y4 = 0.0*/
  def transform (transformation:Coord=>Coord): Some[BezierLine] = {
    val res = new BezierLine(this.color,this.size)
    res.setCoords(this.coords.map(c=>transformation(c)))
    Some(res)
  }
  def getCoordAt(dist:Double):Coord={
    if(dist<=0)return coords.head
    if(dist>=1)return coords.last
    Bezier.pointAt(dist,coords(0),coords(1),coords(2),coords(3))
  }
  def getCoord (ind:Int): Coord ={
    if(ind>=0&&ind<4)
      coords(ind) else coords(0)
  }
  def getCoords: Array[Coord] ={
      coords
  }
  def setCoord(ind:Int,place:Coord){
    if(ind>=0&&ind<4) {
      coords(ind) = place
      changed = true
    }
  }
  def setCoords(places:Array[Coord]){
    val maxi = math.min( places.length,4)
    for(i<-0 until maxi){
      coords(i) = places(i)
    }
    changed = true
  }
  def distFromHandles(point:Coord): Double = (this.coords ++ this.getLine.getCoords).map(_.dist(point)).min
  def distFromCenter(point:Coord): Double ={
    math.min(this.getLine.distFromCenter(point), distFromHandles(point))
  }
  def distFromEdge(point:Coord): Double ={
    math.min(this.getLine.distFromEdge(point), distFromHandles(point)-1)
  }
  def getLine:BasicLine = {
    if(changed){
      basicline = this.getLine(color,size)
      changed = false
    }
    basicline
  }
  def getLine(color1:Color,size1:Double):BasicLine = {
    if(coords.forall(_==coords(0))){ //if the bezier line is a point, return a point
      val st = new BasicLine(color1,size1)
      st.setCoords( Array(coords(0)) )
      return st
    }
    val cs = Bezier.curve(coords(0),coords(1),coords(2),coords(3))
    val res = new BasicLine(color1,size1)
    res.setCoords (cs.map { c => c.rounded(Magic.roundingAccuracy) })
    res
  }
  def getLines : Array[BasicLine]= {
    val res = getLine
    //res.xs = xys._1.map(x=>math.round(2*x)/2.0)
    //res.ys = xys._2.map(y=>math.round(2*y)/2.0)
    Array(res)
  }
  def selection: Some[MultiLine] = {
    val res = new MultiLine
    res.addLine(this.getLine(Colors.inverse(color),1))
    val line = new BasicLine(Colors.inverse(color),1)
    line.setCoords(coords)
    res.addLine(line)
    Some(res)
  }
  def toJson: Some[JsonStroke] = {
    val json = new JsonStroke
    json.color = Colors.toHexString(color)
    json.size = size
    json.coords = coords.map(_.toJson)
    json.linetype = "bezier"
    Some(json)
  }
  def toJsonString: Some[String] = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"linetype\":\"bezier\",\"color\":\""+Colors.toHexRGBA(color)+"\",\"size\":"+sizestr+",\"coords\":["+coords.map(_.toJsonString).mkString(",")+"]}")
  }
  def toShortJsonString: Some[String] = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"l\":\"b\",\"c\":\""+Colors.toHexRGBA(color)+"\",\"s\":"+sizestr+",\"p\":["+coords.map(_.toShortJsonString).mkString(",")+"]}")
  }
  def toSVGString: String = {
    this.getLine.toSVGString
  }
}