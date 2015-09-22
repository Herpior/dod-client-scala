package dmodel

import math.{hypot,round}

object Coord{
  def apply(c:Double) :Coord =Coord(c,c)
}

case class Coord(val x:Double,val y:Double) {

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
  def angle(other:Coord)={
    Angle.angle(this.x-other.x, this.y-other.y)
  }
  def rounded(accuracy:Int)={
    Coord(round(this.x*accuracy)/accuracy.toDouble,round(this.y*accuracy)/accuracy.toDouble)
  }
  override def toString={
    "("+x+", "+y+")"
  }
}