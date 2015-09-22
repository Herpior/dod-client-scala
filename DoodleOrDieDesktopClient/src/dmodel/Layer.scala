package dmodel

import collection.mutable.Buffer

class Layer() {
  val redos = Buffer[DoodlePart]()
  //var img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB)
  //var thumb = new BufferedImage(200,150,BufferedImage.TYPE_INT_ARGB)
  private val strokes = Buffer[DoodlePart]()
  private var visible = true
  private var selected = false
  
  def isVisible = visible
  def isSelected = selected
  
  def select = selected = !selected
  def visibility = visible = !visible
  
  def setVisibility(next:Boolean) {visible = next}
  
  def load(doodle:JsonDoodle){
    this.strokes ++= doodle.getStrokes.map(_.toBasicLine)
  }
  def load(arr:Array[JsonLine]){
    this.strokes ++= arr.map(_.toBasicLine)
  }
  def merge(another:Layer){
    this.strokes ++= another.getThumb
    this.redos.clear
    this.redos ++= another.redos
  }
  def add(stroke:DoodlePart){
    strokes += stroke
    //redos--=redos
    redos.clear()
    //println(redos.length)
  }
  def undo {
    if(strokes.length>0){
      redos += strokes.last
      strokes -= strokes.last
    }
  }
  def redo {
    if(redos.length>0){
      strokes += redos.last
      redos -= redos.last
    }
  }
  def burn {
    while (strokes.length>0){
      undo
    }
  }
  def revive {
    while(redos.length>0){
      redo
    }
  }
  def split = {
    val upper = new Layer
    upper.redos ++= this.redos
    this.redos.clear
    upper.revive
    upper.setVisibility(false)
    upper
  }
  def getStrokes={
    if(visible)strokes.toArray else Array[DoodlePart]()
  }
  def getThumb={
    strokes.toArray
  }
}