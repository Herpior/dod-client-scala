package dmodel

import math.{hypot,round}

object Coord{
  def apply(c:Double) :Coord =Coord(c,c)
}

case class Coord(val x:Double,val y:Double) extends Metric[Coord] {

  def dist(other:Coord)={
    hypot(this.x-other.x,this.y-other.y)
  }
  def +(other:Coord)={
    Coord(this.x+other.x,this.y+other.y)
  }
  def -(other:Coord)={
    Coord(this.x-other.x,this.y-other.y)
  }
  def <=(other:Coord)={ //true if both axes less or equal than other
    this.x<=other.x && this.y<=other.y
  }
  def >=(other:Coord)={ //true if both axes more or equal than other
    this.x>=other.x && this.y>=other.y
  }
  //def /(other:Coord)={
  //  Coord(this.x/other.x,this.y/other.y)
  //}
  def /(divisor:Double)={
    Coord(this.x/divisor,this.y/divisor)
  }
  def *(multiplier:Double)={
    Coord(this.x*multiplier,this.y*multiplier)
  }
  def unary_- ={
    Coord(-this.x,-this.y)
  }
  def abs={
    Coord(math.abs(this.x), math.abs(this.y))
  }
  def length = {
    hypot(this.x, this.y)
  }
  def max = {
    math.max(this.x, this.y)
  }
  def angle(other:Coord)={
    Angle.angle(this.x-other.x, this.y-other.y)
  }
  def rounded(accuracy:Int)={
    Coord(round(this.x*accuracy)/accuracy.toDouble,round(this.y*accuracy)/accuracy.toDouble)
  }
  def flip={ //flips x and y
    Coord(this.y, this.x)
  }
  def perpendiculate={ //flips vector 90 degrees
    Coord(-this.y, this.x)
  }
  def toAngle={//returns direction as angle in radians
    math.tan(x/y)
  }
  def toArray={
    Array(x, y)
  }
  def toJson={
    val json = new JsonCoord
    json.x = x
    json.y = y
    json
  }
  override def toString={
    "("+x+", "+y+")"
  }
  def toJsonString = {
    val xx = if(x%1==0)math.round(x).toString else x.toString
    val yy = if(y%1==0)math.round(y).toString else y.toString
    "{\"x\":"+xx+",\"y\":"+yy+"}"
  }
  def toShortJsonString = {
    val xx = if(x%1==0)(x).toInt.toString else x.toString
    val yy = if(y%1==0)(y).toInt.toString else y.toString
    xx+","+yy
  }

}

