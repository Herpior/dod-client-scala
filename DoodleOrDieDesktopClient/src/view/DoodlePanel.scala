package view

import scala.swing._
import java.awt.RenderingHints
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.Toolkit
import java.io.File
import dmodel.LineDrawer
import dmodel.DoodleModel
import dmodel.SizeModel
import dmodel.DoodlePart
import dmodel.Coord
import dmodel.Magic
//import java.awt.Cursor
import math.{max,min}

class DoodlePanel extends Panel {
  val pencil = Toolkit.getDefaultToolkit.createCustomCursor(io.Icons.getPenCursor, new java.awt.Point(0,0), "pencil")
  this.cursor = pencil
  this.minimumSize = new Dimension(400,300)
  this.preferredSize = new Dimension(2*Magic.x,2*Magic.y)
  this.maximumSize = new Dimension(4320,3240)
  val model = new DoodleModel
  private val zooms=Array(
      0.25, 0.3,  0.35, 0.42,
      0.5,  0.59, 0.7,  0.84,
      1,    1.19, 1.41, 1.68,
      2,    2.38, 2.82, 3.36,
      4,    4.76, 5.65, 6.72,
      8,    9.5,  11.3, 13.45,
      16,   19,   22.6, 26.9,
      32,   38,   45.2, 53.8)
  private var zoomind = 8//1.0
  private var cursorX = -100
  private var cursorY = -100
  
  private var botImg = createImg
  private var midImg = createImg
  private var topImg = createImg
  private var drawImg = createImg
  
  def getZoom = zooms(zoomind)
  def getWidth = this.bounds.getWidth.toInt
  def getHeight = this.bounds.getHeight.toInt
  def canDraw = !this.model.layers.isMatrix
  
  def getSelected = {
    val img = new BufferedImage(Magic.x*2,Magic.y*2,BufferedImage.TYPE_INT_ARGB)
    LineDrawer.redraw2(
        img, 
        model.layers.getSelected.flatMap(_.getThumb))
    img
  }
  
  private var point = Magic.doodleSize/2
  private var tmp = Coord(0,0)
  
  override def paintComponent(g:Graphics2D){
    if(botImg.getHeight!=this.bounds.getHeight || botImg.getWidth!=this.bounds.getWidth){
      this.redrawAll
      //println("resized")
    }
    g.setColor(Magic.bgColor)//(Magic.bgColor)
    g.fillRect(0, 0, getWidth, getHeight)
    
    val gx = -(tmp.x*this.getZoom).toInt
    val gy = -(tmp.y*this.getZoom).toInt
    //this.super.paintComponent(g)
    g.drawImage(botImg,gx,gy,null)
    g.drawImage(midImg,gx,gy,null)
    g.drawImage(drawImg,gx,gy,null)
    g.drawImage(topImg,gx,gy,null)
    g.setColor(Magic.bgColorAlpha)
    val off = offset
    val canvas = Magic.doodleSize*getZoom
    val inv = Coord(getWidth,getHeight)-off-canvas
    g.fillRect(gx+0, gy+off.y.toInt, off.x.toInt, canvas.y.toInt) 
    g.fillRect(gx+0, gy+0, this.getWidth , off.y.toInt)
    
    g.fillRect(gx+0, gy+off.y.toInt+canvas.y.toInt, getWidth, inv.y.toInt+1)
    g.fillRect(gx+off.x.toInt+canvas.x.toInt, gy+off.y.toInt, inv.x.toInt+1, canvas.y.toInt)
    
    //g.setColor(Magic.red)
    //val points = model.pers.getPoints
    //points.foreach(p=>g.fillOval(p.x-1, p.y-1, 3, 3))
    
    //draw selected line if any
    
    //model.selected.foreach{ml=>LineDrawer.drawDoodlePart(g,ml.selection,getZoom,offset,true)}
    //draw the cursor
    
    //val col = model.tools.getColor
    val col_opt = this.pickColor(cursorX, cursorY, true)
    var col = Magic.black
    col_opt.foreach { x => col = dmodel.Colors.inverse(x)}
    
    //val invert = dmodel.Colors.inverse(col)
    g.setColor(col)
    val bsize = max(2,(SizeModel.getSize*getZoom)).toInt
    g.drawOval(cursorX-bsize/2, cursorY-bsize/2, bsize, bsize)
    /*
    g.setColor(Color.black)
    g.drawOval(cursorX-bsize/2+1, cursorY-bsize/2+1, bsize-2, bsize-2)
    */
    g.fillRect(cursorX, cursorY, 1, 1)
  }
  def save(chain:String){
    //this.model.toLocalStorage
    this.model.save(chain)//TODO remove either and follow and remove the things this would use?
  }
  def setCursor(x:Double,y:Double)={
    cursorX = x.toInt
    cursorY = y.toInt
  }
  def createImg={
    new BufferedImage(math.max(this.bounds.getWidth.toInt,400),math.max(this.bounds.getHeight.toInt,300),BufferedImage.TYPE_INT_ARGB)
  }
  // redraws everything
  // very slow if there are a lot of things
  def redrawAll {
    redrawBot
    redrawTop
    redrawMid
    redrawDrawing
  }
  // redraws the layers below the current layer
  // pretty slow if there are a lot of things below
  def redrawBot{ //TODO change to clearRect based solution? that won't bug when area size changes
    val img = createImg  //or graphics.setComposite(AlphaComposite.Clear); graphics.fillRect(0, 0, SIZE, SIZE); graphics.setComposite(AlphaComposite.SrcOver);
    val g = img.createGraphics()
    g.setColor(Magic.bgColor)
    g.fillRect(0, 0, img.getWidth, img.getHeight)
    val off = offset
    val canvas = Magic.doodleSize*getZoom
    g.setColor(Magic.white)
    g.fillRect(off.x.toInt, off.y.toInt, canvas.x.toInt, canvas.y.toInt)
    //LineDrawer.redraw(img, model.getBot)
    model.getBot.flatMap { x => x.getStrokes(true) }.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    botImg = img
  }
  // redraws the current layer
  def redrawMid{
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getMid)
    model.getMid.getStrokes(false).foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    midImg = img
  }
  // redraws the layers on top of the current layer
  // pretty slow if there are a lot of things on top
  def redrawTop{
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getTop)
    model.getTop.flatMap { x => x.getStrokes(true) }.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    topImg = img
  }
  // redraws the line currently being drawn 
  // used for lines that redrawLast can't be used on
  // that is, on bezier lines and semitransparent lines
  // it will also clear the drawing layer but there should be a better way to clear it somewhere?
  def redrawDrawing{
    val img = createImg
    val g = img.createGraphics()
    model.getDrawing.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    drawImg = img
  }
  // redraws image when moving up in the layers
  // that is, it merges the current layer on top of the bottom layer and redraws the current layer and the top layer
  def redrawLayerUp{
    val g = botImg.createGraphics
    g.drawImage(midImg, 0, 0, null)
    redrawTop
    redrawMid
  }
  // redraws image when moving down in the layers
  // that is, it merges the current and the top layers and redraws the new current layer and bottom layers
  def redrawLayerDown{
    val g = midImg.createGraphics
    g.drawImage(topImg, 0, 0, null)
    topImg = midImg
    redrawMid
    redrawBot
  }
  // draw the image again after merging down
  // that is, the current layer and bottom layer are redrawn
  // as an optimization this might be trying to draw the bottom part of the merged layer first, before it's merged and paste the previously current layer on top, but now I'm not sure if this actually does that
  def redrawMergeDown{
    val img = midImg
    redrawMid
    val g = midImg.createGraphics()
    g.drawImage(img, 0, 0, null)
    redrawBot
  }
  // draw the last segment of the current line
  // used when a new segment is added to current line
  // does not work with semitransparent lines or beziers
  def redrawLast{
    val g = drawImg.createGraphics()
    model.getLast.foreach{dp=>
      LineDrawer.drawDoodlePartLast(g,dp,getZoom,offset,true)
    }
  }
  // draw the last line in current layer
  // used when everything else in the layer is drawn
  // that is, when a new line is finished
  def redrawLastMid{
    val g = midImg.createGraphics()
    model.getLastMid.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
  }
  
  def zoomin(num:Int) = {
    val changed = zoomind-num//(zoom*(math.pow(2,-num))*10).toInt/10.0
    if(changed>=0 && changed<zooms.length) zoomind = changed
    //this.publish(new ZoomEvent)
    redrawAll
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
  def move1(coord:Coord) = {//deprecated, use hand tool
    val moved = point+(tmp-coord)/getZoom
    point = Coord(max(min(moved.x,Magic.x),0),max(min(moved.y,Magic.y),0))
    tmp = Coord(0,0)
    //???
  }
  def fullScreen {
    this.publish(new FullscreenEvent)
  }
  def pickColor(x:Int,y:Int, shift:Boolean):Option[Color] = {
    //Todo some mod for picking color from whole image
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
  def offset = {
    //val z = (-1/(zoom/2)+1)
    //val off = magic*z
    //val offX = (260*z).toInt //0.5->-2, 1-> -1, 2->0, 4->0.5, 8->0.75
    //val offY = (195*z).toInt // -1/z+1
    //(Coord(x,y)+(offset*zoom))/zoom
    val middle = Coord (getWidth,getHeight)/2
    val off = middle-point*getZoom
    //println("wt: "+this.getWidth+" ht: "+this.getHeight+" - "+off.x+" off "+off.y)
    off
  }
  def getCoord(x:Double,y:Double)={
    //val middle = Coord (getWidth,getHeight)/2
    val coord = (Coord(x,y)-(offset))/getZoom//(Coord(x,y)-middle)/zoom + (Magic.doodleSize - point)
    //println(coord.x+", "+coord.y)
    coord
  }
  def submit:Boolean={
    if(model.getPaintPercentage<4){
      Dialog.showMessage(this, "How about a more paint there, yes?", "submit", Dialog.Message.Warning, null)
      return false
    }
    if(model.getPaintTime<100000){
      Dialog.showMessage(this, "Spend more time drawing, maybe?", "submit", Dialog.Message.Warning, null)
      return false
    }
    val successful = model.submit
    if(!successful){
      Dialog.showMessage(this, "Failed to submit doodle.", "failure", Dialog.Message.Warning, null)
    }
    successful
  }
  def exportImage(percent:Double){
    val img = new BufferedImage((percent*Magic.x).toInt,(percent*Magic.y).toInt, BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    model.getLayers.foreach{
      lay=>
        lay.getStrokes(true).foreach { 
          stro =>
            LineDrawer.drawDoodlePart(g,stro,percent,Coord(0,0),true)
          }
    }
    //var out: java.io.OutputStream = null
    try{
      //val localfile = "sample2.png"
      //out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(localfile))
      //out.write(img.)
      val path = Dialog.showInput(this, "save location", "save", Dialog.Message.Question, null, List(), "exported.png")
      path.foreach{
        p=>
          val outputfile = new File(p)
          val check = if(outputfile.exists())Dialog.showConfirmation(this, "this file already exists, overwrite?", "overwrite?", Dialog.Options.YesNo, Dialog.Message.Question, null)==Dialog.Result.Yes else true
          if(check)javax.imageio.ImageIO.write(img, "png", outputfile)
      }
    } catch {
        case e:Throwable => e.printStackTrace()
    } finally {
        //out.close
    }
  }
}
