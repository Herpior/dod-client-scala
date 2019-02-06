package dmodel

/**
 * @author Herpior
 */

import collection.mutable.Buffer

class LayerList {
  
  private var layers = Buffer(new Layer)
  private var current = 0
  
  def ind: Int = current
  def isMatrix: Boolean = getCurrent.isMatrix

  def size: Int = layers.length
  //
  def getTop: Array[Layer] = {
    this.layers.drop(ind + 1).toArray
  }
  def getBot: Array[Layer] = {
    this.layers.take(ind).toArray
  }
  def getCurrent: Layer = {
    layers(ind)
  }
  def getSelected: Array[Layer] = {
    val curr = getCurrent
    this.layers.filter { lay => lay.isSelected || lay==curr}.toArray
  }
  def switch(first:Int,other:Int){
    val len = this.layers.length
    if(first!=other&&first>=0&&first<len && other>=0&&other<len){
      val tmp = layers(first)
      layers(first)=layers(other)
      layers(other)=tmp
    }
  }
  def split() {
    val curr = getCurrent
    if(!curr.getRedos.isEmpty)
    addLayer(curr.split)
  }
  //
  def toArray: Array[Layer] = {
    layers.toArray
  }
  def head: Layer = {
    layers.head
  }
  
  //
  def undo(): Unit = {
    layers(ind).undo
  }
  def redo(): Unit = {
    layers(ind).redo
  }
  def burn(): Unit = {
    layers(ind).burn
  }
  
  //
  def mergeLayer(){
    val i = ind
    if(i>0){
      layers(i-1).merge(layers(i))
      layers -= layers(i)
      current -= 1
      }
  }
  def removeLayer(){
    if(ind>=0 && size>1){
      layers -= layers(ind)
      if(ind>0)current -= 1
      }
  }
  def addLayer(){addLayer(new Layer)}
  def addMatrixLayer(orig:Layer){
    addLayer(new MatrixLayer(orig))
    layerUp
    }
  def finaliseMatrix(){
    this.getCurrent match {
      case layer: MatrixLayer =>
        val res = layer.normal
        removeLayer
        addLayer(res)
      case _ =>
    }
  }
  def addLayer(added:Layer){
    val risky = layers.drop(ind+1)
    //layers --= risky
    layers = layers.take(ind+1)
    layers += added
    layers ++= risky
    current += 1
  }
  def addLayers(adding:Buffer[Layer]){
    adding.foreach { l => addLayer(l) }
  }
  def layerUp(){
    if(ind+1<layers.length) {
      //finaliseMatrix
      current += 1
    }
  }
  def layerDown(){
    if(ind>0) {
      //finaliseMatrix
      current -= 1
    }
  }
  def setCurrent(index:Int){
    if(index>=0&&index<size){
      //finaliseMatrix
      current = index
    }
  }
  def load(save:JsonSave){
    if(size==1 && layers.head.getStrokes.length==0) layers = save.getDoodleLayers
    else layers ++= save.getDoodleLayers
    current = size-1
  }
  def toJson(time:Int): String = {
    JsonParse.writeSave(this.toArray, time)
  }
  def toJsonString(time:Int, chain:String): String = {
    "{\"version\":"+Magic.version+",\"doodle_id\":\""+chain+"\",\"time\":"+time+",\"layers\":["+this.layers.map(_.toJsonString).mkString(",")+"]}"
   }
  def toShortJsonString(time:Int, chain:String): String = {
    "{\"v\":"+Magic.version+",\"d\":\""+chain+"\",\"t\":"+time+",\"l\":["+this.layers.map(_.toShortJsonString).mkString(",")+"]}"
   }
  
}