package dmodel

/**
 * @author Herpior
 */

import collection.mutable.Buffer

class LayerList {
  
  private var layers = Buffer(new Layer)
  private var current = 0
  
  def ind = current
  
  def size = layers.length
  //
  def getTop = {
    this.layers.drop(ind + 1).toArray
  }
  def getBot = {
    this.layers.take(ind).toArray
  }
  def getCurrent = {
    layers(ind)
  }
  def getSelected = {
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
  def split {
    val curr = getCurrent
    if(!curr.redos.isEmpty)
    addLayer(curr.split)
  }
  //
  def toArray = {
    layers.toArray
  }
  def head = {
    layers.head
  }
  
  //
  def undo = {
    layers(ind).undo
  }
  def redo = {
    layers(ind).redo
  }
  def burn = {
    layers(ind).burn
  }
  
  //
  def mergeLayer{
    val i = ind
    if(i>0){
      layers(i-1).merge(layers(i))
      layers -= layers(i)
      current -= 1
      }
  }
  def removeLayer{
    if(ind>=0 && size>1){
      layers -= layers(ind)
      if(ind>0)current -= 1
      }
  }
  def addLayer{addLayer(new Layer)}
  def addMatrixLayer(orig:Layer){
    addLayer(new MatrixLayer(orig))
    layerUp
    }
  def finaliseMatrix{
    if(this.getCurrent.isInstanceOf[MatrixLayer]){
      val res = this.getCurrent.asInstanceOf[MatrixLayer].normal
      removeLayer
      addLayer(res)
    }
  }
  def addLayer(added:Layer){//TODO: check that this works properly!!
    val risky = layers.drop(ind+1)
    //layers --= risky
    layers = layers.take(ind+1)
    layers += added
    layers ++= risky
  }
  def layerUp{
    if(ind+1<layers.length) {
      //finaliseMatrix
      current += 1
    }
  }
  def layerDown{
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
    layers ++= save.getLayers
  }
  def toJson(time:Int) = {
    JsonParse.writeSave(this.toArray, time)
  }
  
}