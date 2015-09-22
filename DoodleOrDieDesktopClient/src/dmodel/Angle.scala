package dmodel

import math._

object Angle {

  // y increases downwards, x increases to right.
  def angle(x: Double, y: Double) = {
    val upRight = atan(abs(y)/abs(x))
    val xDir = x >= 0
    val yDir = y > 0
    val directionMap = Map((true, true) -> 0.0, (false, false) -> Pi, (true, false) -> (2*Pi + 2*(-upRight)), (false, true) -> (Pi-2*upRight))
    upRight + directionMap((xDir, yDir))
  }
  
  def getXY(angle: Double, speed: Double) = {
    val x = speed * cos(angle)
    val y = speed * sin(angle)
    (x , y)
  }
  def getCoord(angle: Double, speed: Double) = {
    val x = speed * cos(angle)
    val y = speed * sin(angle)
    new Coord(x , y)
  }
  
  def compare(one:Double,another:Double) = {//10-350=-340 =>20 || 300-50=250 =>-110
    var res = one-another//if(one<Pi&&another>Pi)one-another+2*Pi
    while(res>Pi)res-=2*Pi
    while(res< -Pi)res+=2*Pi
    res
  }
  
  def flip(flipping:Double,flipper:Double) = {
    //0<flipper<Pi 
    val temp = Pi/2-flipper
    val flipped = Pi-(flipping+temp)
    flipped-temp
  }
  /*
  var flag = true
  while (flag){
    val input = readLine(Vector("1) Laske kulma", "2) Laske X ja Y", "0) Lopetus", "Valinta: ").mkString("\n"))
    if(input == "1") {
      val X = readLine("X: ").toDouble
      val Y = readLine("Y: ").toDouble
      println("Kulma on: " + angle(X, Y)*360/(2*Pi) + " astetta. Nopeus on : "+hypot(X,Y)+".")
      }
    else if (input == "2"){
      val angle = readLine("Angle: ").toDouble.toRadians
      val length = readLine("Length: ").toDouble
      val xy = getXY(angle, length)
      println("X on" + xy._1, "ja Y on" + xy._2+".")
      }
    else if (input == "0"){
      flag = false
    }

  }*/
  
}