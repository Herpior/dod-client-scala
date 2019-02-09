package dmodel

/**
  * A class that contains all the buffered images in a doodle.
  * handles zoom and offset to convert coordinates between screen space and image space.
  *

  * @author Qazhax
  */

import java.awt.Color
import java.awt.image.BufferedImage

class DoodleBufferer(val model:DoodleModel, private var width:Int, private var height:Int) {

  // possible zoom values are set in an array to avoid losing accuracy when repeatedly zooming in and out
  // TODO: find a translation between integers from 0 to 32 and the values in the array
  private val zooms=Array(
    0.25, 0.3,  0.35, 0.42,
    0.5,  0.59, 0.7,  0.84,
    1,    1.19, 1.41, 1.68,
    2,    2.38, 2.82, 3.36,
    4,    4.76, 5.65, 6.72,
    8,    9.5,  11.3, 13.45,
    16,   19,   22.6, 26.9,
    32,   38,   45.2, 53.8,
    64)
  private var zoomInd = 8//1.0

  var botImg: BufferedImage = createImg
  var midImg: BufferedImage = createImg
  var topImg: BufferedImage = createImg
  var drawImg: BufferedImage = createImg

  var dirty = true
  private var point = Magic.doodleSize/2
  private var tmp = Coord(0,0)

  def setBounds(w:Int, h:Int): Unit = {
    if(width != w || height != h){
      dirty = true
      width = w
      height = h
    }
  }

  def getZoom = zooms(zoomInd)
  def getTmp: Coord = tmp

  def canDraw: Boolean = !this.model.layers.isMatrix

  def createImg: BufferedImage ={
    new BufferedImage(math.max(width,400),math.max(height,300),BufferedImage.TYPE_INT_ARGB)
  }


  // redraws everything
  // very slow if there are a lot of things
  def redrawAll() {
    redrawBot
    redrawTop
    redrawMid
    redrawDrawing
    dirty = false
  }
  // redraws the layers below the current layer
  // pretty slow if there are a lot of things below
  def redrawBot(){
    //println("redrawBot")
    val img = createImg  //or graphics.setComposite(AlphaComposite.Clear); graphics.fillRect(0, 0, SIZE, SIZE); graphics.setComposite(AlphaComposite.SrcOver);
    val g = img.createGraphics()
    g.setColor(Magic.bgColor)
    g.fillRect(0, 0, img.getWidth, img.getHeight)
    val off = offset
    val canvas = Magic.doodleSize*getZoom
    g.setColor(Magic.white)
    g.fillRect(off.x.toInt, off.y.toInt, canvas.x.toInt, canvas.y.toInt)
    model.getBot.flatMap { x => x.getVisibleStrokes(true) }.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    botImg = img
  }
  // redraws the current layer
  // bit slow it there are a lot of things on the layer
  def redrawMid(){
    //println("redrawMid")
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getMid)
    model.getMid.getVisibleStrokes(false).foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    midImg = img
  }
  // redraws the layers on top of the current layer
  // pretty slow if there are a lot of things on top
  def redrawTop(){
    //println("redrawTop")
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getTop)
    model.getTop.flatMap { x => x.getVisibleStrokes(true) }.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    topImg = img
  }
  // redraws the line currently being drawn
  // used for lines that redrawLast can't be used on
  // that is, on bezier lines and semitransparent lines
  // it will also clear the drawing layer but there should be a better way to clear it somewhere?
  def redrawDrawing(){
    //println("redrawDrawing")
    val img = createImg
    val g = img.createGraphics()
    model.getDrawing.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    drawImg = img
  }
  // redraws image when moving up in the layers
  // that is, it merges the current layer on top of the bottom layer and redraws the current layer and the top layer
  def redrawLayerUp(){
    //println("redrawLayerUp")
    val g = botImg.createGraphics
    g.drawImage(midImg, 0, 0, null)
    redrawTop
    redrawMid
  }
  // redraws image when moving down in the layers
  // that is, it merges the current and the top layers and redraws the new current layer and bottom layers
  def redrawLayerDown(){
    //println("redrawLayerDown")
    val g = midImg.createGraphics
    g.drawImage(topImg, 0, 0, null)
    topImg = midImg
    redrawMid
    redrawBot
  }
  // draw the image again after merging down
  // that is, the current layer and bottom layer are redrawn
  // as an optimization this might be trying to draw the bottom part of the merged layer first, before it's merged and paste the previously current layer on top, but now I'm not sure if this actually does that
  def redrawMergeDown(){
    //println("redrawMergeDown")
    val img = midImg
    redrawMid
    val g = midImg.createGraphics()
    g.drawImage(img, 0, 0, null)
    redrawBot
  }
  // draw the last segment of the current line
  // used when a new segment is added to current line
  // does not work with semitransparent lines or beziers
  def redrawLast(){
    //println("redrawLast")
    val g = drawImg.createGraphics()
    model.getLast.foreach{dp=>
      LineDrawer.drawDoodlePartLast(g,dp,getZoom,offset,true)
    }
  }
  // draw the last line in current layer
  // used when everything else in the layer is drawn
  // that is, when a new line is finished
  // TODO: make version that just moves the drawing onto mid and clears drawing to save tiny bit of time?
  def redrawLastMid(){
    //println("redrawLastMid")
    val g = midImg.createGraphics()
    model.getLastMid.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
  }

  def zoomIn(num:Int): Unit = {
    val changed = zoomInd-num//(zoom*(math.pow(2,-num))*10).toInt/10.0
    if(changed>=0 && changed<zooms.length) zoomInd = changed
    //this.publish(new ZoomEvent)
    dirty = true
  }
  def movePanPoint(coord:Coord){
    point += coord
    tmp = Coord(0,0)
  }
  def startMove(){
    tmp = Coord(0,0)
  }
  def prepareMove(coord:Coord){
    tmp = coord
  }

  def offset: Coord = {
    //val z = (-1/(zoom/2)+1)
    //val off = magic*z
    //val offX = (260*z).toInt //0.5->-2, 1-> -1, 2->0, 4->0.5, 8->0.75
    //val offY = (195*z).toInt // -1/z+1
    //(Coord(x,y)+(offset*zoom))/zoom
    val middle = Coord (width, height)/2
    val off = middle-point*getZoom
    //println("wt: "+this.getWidth+" ht: "+this.getHeight+" - "+off.x+" off "+off.y)
    off
  }

  def getCoord(x:Double,y:Double): Coord ={
    //val middle = Coord (getWidth,getHeight)/2
    val coord = (Coord(x,y)- offset)/getZoom//(Coord(x,y)-middle)/zoom + (Magic.doodleSize - point)
    //println(coord.x+", "+coord.y)
    coord
  }

  def pickColor(x:Int,y:Int, shift:Boolean):Option[Color] = {
    if(x>0&&y>0 && x<this.midImg.getWidth&&y<this.midImg.getHeight)
    {
      if(shift) { //picks the visible colour or something like it, I'm not sure if it's the fastest way but whatever, it should work?
        val draw =  new Color(this.drawImg.getRGB(x, y), true)
        val top =  new Color(this.topImg.getRGB(x, y), true)
        val mid =  new Color(this.midImg.getRGB(x, y), true)
        val bot =  new Color(this.botImg.getRGB(x, y), true)
        val base = Color.white
        val mix_img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)
        val g = mix_img.getGraphics
        g.setColor(base)
        g.drawRect(0, 0, 1, 1)
        g.setColor(bot)
        g.drawRect(0, 0, 1, 1)
        g.setColor(mid)
        g.drawRect(0, 0, 1, 1)
        g.setColor(top)
        g.drawRect(0, 0, 1, 1)
        g.setColor(draw)
        g.drawRect(0, 0, 1, 1)
        Some(new Color(mix_img.getRGB(0, 0), true))
      }
      else Some(new Color(this.midImg.getRGB(x, y),true)) //picks colour from current layer only
    }
    else None
  }


  def getSelected: BufferedImage = {
    val img = new BufferedImage(Magic.x*2,Magic.y*2,BufferedImage.TYPE_INT_ARGB)
    LineDrawer.redraw2(
      img,
      model.layers.getSelected.flatMap(_.getStrokes))
    img
  }


}
