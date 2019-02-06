package dmodel

import math.{hypot,round}

object Coord{
  def apply(c:Double) :Coord =Coord(c,c)
  def fromAngle(angle: Double, length: Double):Coord ={
    val x = length * math.cos(angle)
    val y = length * math.sin(angle)
    new Coord(x , y)
  }
}

case class Coord(x:Double, y:Double) extends Metric[Coord] {

  def unary_- ={
    this.map(-_)
    //Coord(-this.x,-this.y)
  }
  def +(other:Coord)={
    this.mapWith(_+_, other)
  }
  def -(other:Coord)={
    this.mapWith(_-_, other)
  }
  def /(other:Coord)={
    this.mapWith(_/_,other)
  }
  def /(divisor:Double)={
    this.map(_/divisor)
  }
  def *(multiplier:Double)={
    this.map(_*multiplier)
  }
  def *(other:Coord)={
    this.mapWith(_*_, other)
  }
  def ^(power:Double)={
    this.map(math.pow(_, power))
  }
  def <(other:Coord)={ //true if both axes less than in other
    this.x<other.x && this.y<other.y
  }
  def >(other:Coord)={ //true if both axes more than in other
    this.x>other.x && this.y>other.y
  }

  def sqr ={
    this*this
  }
  def abs={
    this.map(math.abs(_))
  }
  def max = {
    math.max(this.x, this.y)
  }
  def min = {
    math.min(this.x, this.y)
  }
  def sum ={
    this.x + this.y
  }
  def dot(other:Coord) ={
    (this*other).sum
  }
  def direction(other:Coord)={
    (other-this).toAngle
  }
  def toAngle:Double ={//returns direction as angle in radians
    val ang = this.angleBetweenVector(Coord(1,0))
    if( this.y >= 0 ) ang
    else 2*math.Pi - ang
  }
  def angleBetweenVector(other:Coord) = {
    math.acos(this.normalized.dot(other.normalized))
  }

  def length = {
    hypot(this.x, this.y)
  }
  def lengthSquared ={
    this.sqr.sum
  }
  def dist(other:Coord)={
    (this-other).length
  }
  def distSquared(other:Coord)={
    (this-other).lengthSquared
  }
  // from https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
  def distFromContinuousLine(pointA:Coord, pointB:Coord):Double={
    if(pointA == this || pointB == this) return 0.0
    val delta = pointB - pointA
    val numerator = delta.y*this.x - delta.x*this.y + pointB.x*pointA.y - pointB.y*pointA.x
    math.abs(numerator) / math.sqrt(delta.sqr.sum)
  }
  // from https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
  def distFromLine(pointA:Coord, pointB:Coord):Double ={
    val l2 = pointA.distSquared(pointB)
    if(l2 == 0) return this.dist(pointA)
    val delta = pointB - pointA
    val t = math.max(0, math.min(1, (this-pointA).dot(delta)/l2))
    val proj = pointA + delta*t
    this.dist(proj)
  }

  def rounded(accuracy:Int)={
    this.map(x=>round(x*accuracy)/accuracy.toDouble)
  }
  def normalized:Coord ={
    if(this == Coord(0)) return this
    this/this.length
  }
  def flipped={ //flips x and y
    Coord(this.y, this.x)
  }
  def perpendiculated={ //flips vector 90 degrees
    Coord(-this.y, this.x)
  }

  def map(func:Double=>Double) ={
    Coord(func(this.x), func(this.y))
  }
  def mapWith(func:(Double, Double)=>Double, other:Coord) ={
    Coord(func(this.x, other.x), func(this.y, other.y))
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
    val (xx, yy) = this.toCleanStrings
    "("+xx+", "+yy+")"
  }
  def toJsonString = {
    val (xx, yy) = this.toCleanStrings
    "{\"x\":"+xx+",\"y\":"+yy+"}"
  }
  def toShortJsonString = {
    val (xx, yy) = this.toCleanStrings
    xx + "," + yy
  }
  def toCleanStrings = {
    val xx = if(x%1==0) x.toInt.toString else x.toString
    val yy = if(y%1==0)(y).toInt.toString else y.toString
    (xx,yy)
  }

}

