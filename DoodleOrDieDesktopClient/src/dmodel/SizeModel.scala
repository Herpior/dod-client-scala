package dmodel

import math.max
import math.min

object SizeModel {
  
  val sizes = Vector(1,3,5,10,25,50,100,600)
  private var nextsize = 25
  private var slider = false
  def maxSize = if(Magic.authorized)600 else 100
  def getSize = nextsize
  def setSize(size:Int){ nextsize = min(max(size,1),maxSize) }
  
  def changingSize = slider
  def pressSlider{ slider = true}
  def releaseSlider{ slider = false}
  
  def sizeUp = if(this.nextsize < maxSize) {
    this.nextsize += 1
  }
  def sizeDown = if(this.nextsize>1){
    this.nextsize -= 1
  }
  
  def number(num:Int) = if(num>=0 && num<8){
    if(num<7||Magic.authorized)
    nextsize = sizes(num)
  }
  
}