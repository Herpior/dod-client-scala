package dmodel

import math.max
import math.min

object SizeModel {
  
  val sizes = Vector(1,3,5,10,25,50,100,400)
  private var currentSize = 25
  private var slider = false
  def maxSize = if(Magic.authorized)400 else 100
  def getSize = currentSize
  def setSize(size:Int){ currentSize = min(max(size,1),maxSize) }
  
  def changingSize = slider
  def pressSlider{ slider = true}
  def releaseSlider{ slider = false}
  
  def sizeUp = if(this.currentSize < maxSize) {
    this.currentSize += 1
  }
  def sizeDown = if(this.currentSize>1){
    this.currentSize -= 1
  }
  
  def number(num:Int) = if(num>=0 && num<8){
    if(num<7||Magic.authorized)
    currentSize = sizes(num)
  }
  
}