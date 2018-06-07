package dmodel

import collection.mutable.Buffer
import http.HttpHandler
import io.LocalStorage
import dmodel.tools.BasicTool

class DoodleModel {
  val layers = new LayerList//Buffer(new Layer)
  val tools = ToolModel
  //private val next = new CurrentLine
  
  //private var multiLine:Option[MultiLine] = None
  //private var bezierLine:Option[BezierLine] = None
  
  //private val currentLines:Buffer[DoodlePart] = Buffer()
  
  private def currentTool:BasicTool = tools.getTool
  private def currentLines:Buffer[DoodlePart] = currentTool.getLines
  private def matrix:Boolean = this.layers.getCurrent.isInstanceOf[MatrixLayer]
  //private var textLine:Option[TextLine] = None
  private var hoveringLine:Option[DoodlePart] = None
  private var hoveringLine2:Option[DoodlePart] = None
  
  //private var bezier = false
  
  //private var room:String = "global"
  //private var id:String = ""
  private var painttime = 0
  
  //private var current = 0
  //private var state = 0
  //private var size = 25
  //private var color = "#000000"
  //private var color2 = "#000000"
  
  def selected = (hoveringLine ++ hoveringLine2).toArray
  //def isWriting = textLine.isDefined
  def isDrawing = currentLines.length>0/*multiLine.isDefined*/ || hoveringLine2.isDefined
  //def isBezier = bezier && bezierLine.isDefined
  def isMatrix = {
    matrix
  }
  //---------\\
  def addTime(time:Long){
    //println(painttime)
    painttime += time.toInt
    if(painttime < 0) painttime = 1000000}
  //def setState(tool:Int) {state = tool}
  //def setSize(next:Int) {size = next}
  /*def setColor(col:String,ind:Int){
    if(ind<1)color = col else color2 = col
  }*/
  //---------\\
  //def getState = state
  //def getSize = size
  //def getColor = color
  //---------\\
  def startDrawing(dp:DoodlePart){
    this.currentLines.clear()
    this.currentLines += dp
  }
  def startDrawing(dps:Array[DoodlePart]){
    this.currentLines.clear()
    this.currentLines ++= dps
  }
  //---------\\
  def getTop = {
    layers.getTop//drop(current+1).flatMap { x => x.getStrokes }.toArray
  }
  def getBot = {
    layers.getBot//.take(current).flatMap { x => x.getStrokes }.toArray
  }
  def getMid = {
    layers.getCurrent//(current).getStrokes
  }
  //---------\\
  def getDrawing = {
    currentLines.toArray//(bezierLine/*++textLine*/++multiLine).toArray
  }
  def getLast = {
  //  multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
    currentTool.getLastLine
  }
  def getLastMid = {
    val strokes = layers.getCurrent.getStrokes(false)//(current).getStrokes
    strokes.lastOption
  }
  def getLayers = {
    layers.toArray
  }
  //---------\\
  def load(loaded:JsonDoodle){
    layers.head.load(loaded)
  }
  def loadFrom(chain:String){
    try{
      val loaded = io.LocalStorage.loadSave(chain)//.decryptFrom(path,chain)
      val parsed = JsonParse.parseSave(loaded)
      layers.load(parsed)
      //layers.getCurrent/*(this.current)*/.load(loaded._1)
      painttime=parsed.time
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def decryptFrom(path:String,chain:String){
    try{
      val loaded = io.LocalStorage.decryptFrom(path,chain)
      layers/*.getCurrent(this.current)*/.addLayers(loaded._1)
      addTime(loaded._2)
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def submit={
    HttpHandler.submitDoodle(this.getPaintPercentage, this.getPaintTime.toInt, layers.toArray.flatMap(_.getStrokes(true).flatMap(_.getLines)))
  }
  def save={
    LocalStorage.saveTo(layers.toShortJsonString(this.getPaintTime), http.HttpHandler.getChain)
  }
  //---------\\
  def getPaintPercentage={
    //println("pp "+LineDrawer.paintPercentage(layers.toArray))
    LineDrawer.paintPercentage(layers.toArray)
    //???
  }
  def getPaintTime={
    //println("pt "+painttime)
    painttime
    //???
  }
  //---------\\
  /*def addChar(c:Char){
    ???
  }
  def addString(str:String){
    ???
  }
  def backspace{
    ???
  }
  def deleteChar{
    ???
  }*/
  //---------\\
  /*def copy{
    ???
  }
  def paste{
    ???
  }*/
  def undo{
    layers.undo
  }
  def redo{
    layers.redo
  }
  def burn{
    layers.burn
  }
  //---------\\
  /*def mouseMoved(place:Coord,mods:Int){
    ???
  }
  def mouseDragged(place:Coord,mods:Int){
    ???
  }
  def mousePressed(place:Coord,mods:Int){
    ???
  }
  def mouseReleased(place:Coord,mods:Int){
    ???
  }*/
  //---------\\
  def mergeLayer{
    layers.mergeLayer
  }
  def addLayer{
    layers.addLayer
  }
  def layerUp{
    layers.layerUp
  }
  def layerDown{
    layers.layerDown
  }
  //---------\\
  def toLocalStorage{
    io.LocalStorage.printFile(layers.toArray.flatMap(_.getStrokes(true).flatMap(_.getLines)),HttpHandler.getChain,this.getPaintTime.toInt, "backup."+HttpHandler.getGroup+".txt")
  }
  //---------\\
  /*def startPerspective(place:Coord,mods:Int){
  }*/
  def dragPerspective(place:Coord,mods:Int){
    if(mods/128%2==1)Perspective.setTertiary(place)
    else if(mods/512%2==1)Perspective.setSecondary(place)
    else Perspective.setPrimary(place)
  }
  def removePerspective(mods:Int){
    if(mods/128%2==1)Perspective.removeTertiary
    else if(mods/512%2==1)Perspective.removeSecondary
    else Perspective.removePrimary
  }
  //---------\\
  /*def startWriting(place:Coord){
    ???
  }
  def dragWriting(place:Coord,mods:Int){
    ???
  }
  def stopWriting{
    ???
  }*/
  
  //---------\\
  def matrixLayer {
    if(!matrix)this.layers.addMatrixLayer(this.layers.getCurrent)
    else this.layers.finaliseMatrix
  }
  def startMatrix(place:Coord,mods:Int){
    layers.getCurrent.pressPoint(place)
  }
  def dragMatrix(place:Coord,mods:Int){
    layers.getCurrent.dragPoint(place)
  }
  def stopMatrix{
    layers.getCurrent.releasePoint
  }
}