package view

import scala.swing._
import dmodel.LineDrawer
import dmodel.DoodleModel
import dmodel.DoodlePart
import dmodel.Coord
import dmodel.Magic
import java.awt.RenderingHints
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
//import java.awt.Cursor
import java.awt.Toolkit
import math.{max,min}

class DoodlePanel extends Panel {
  val pencil = Toolkit.getDefaultToolkit.createCustomCursor(io.Icons.getPenCursor, new java.awt.Point(0,0), "pencil")
  this.cursor = pencil
  this.minimumSize = new Dimension(400,300)
  this.preferredSize = new Dimension(2*Magic.x,2*Magic.y)
  this.maximumSize = new Dimension(4320,3240)
  val magic = Magic.doodleSize
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
  
  def getSelected = {
    val img = new BufferedImage(Magic.x*2,Magic.y*2,BufferedImage.TYPE_INT_ARGB)
    LineDrawer.redraw2(
        img, 
        model.layers.getSelected.flatMap(_.getThumb))
    img
  }
  
  private var point = magic/2
  private var tmp = point
  
  override def paintComponent(g:Graphics2D){
    if(botImg.getHeight!=this.bounds.getHeight || botImg.getWidth!=this.bounds.getWidth){
      this.redrawAll
      //println("resized")
    }
    g.setColor(Magic.bgColor)//(Magic.bgColor)
    g.fillRect(0, 0, getWidth, getHeight)
    //this.super.paintComponent(g)
    g.drawImage(botImg,0,0,null)
    g.drawImage(midImg,0,0,null)
    g.drawImage(drawImg,0,0,null)
    g.drawImage(topImg,0,0,null)
    g.setColor(Magic.bgColorAlpha)
    val off = offset
    val canvas = Magic.doodleSize*getZoom
    val inv = Coord(getWidth,getHeight)-off-canvas
    g.fillRect(0, off.y.toInt, off.x.toInt, canvas.y.toInt) 
    g.fillRect(0, 0, this.getWidth , off.y.toInt)
    
    g.fillRect(0, off.y.toInt+canvas.y.toInt, getWidth, inv.y.toInt+1)
    g.fillRect(off.x.toInt+canvas.x.toInt, off.y.toInt, inv.x.toInt+1, canvas.y.toInt)
    
    //g.setColor(Magic.red)
    //val points = model.pers.getPoints
    //points.foreach(p=>g.fillOval(p.x-1, p.y-1, 3, 3))
    
    //draw selected line if any
    
    model.selected.foreach{ml=>LineDrawer.drawDoodlePart(g,ml.selection,getZoom,offset,true)}
    //draw the cursor
    
    //val col = model.tools.getColor
    //val invert = dmodel.Colors.inverse(col)
    g.setColor(Magic.black)
    val bsize = max(2,(model.tools.getSize*getZoom)).toInt
    g.drawOval(cursorX-bsize/2, cursorY-bsize/2, bsize, bsize)
    /*
    g.setColor(Color.black)
    g.drawOval(cursorX-bsize/2+1, cursorY-bsize/2+1, bsize-2, bsize-2)
    */
    g.fillRect(cursorX, cursorY, 1, 1)
  }
  def save{
    //this.model.toLocalStorage
    this.model.save//TODO remove either and follow and remove the things this would use?
  }
  def setCursor(x:Double,y:Double)={
    cursorX = x.toInt
    cursorY = y.toInt
  }
  def createImg={
    new BufferedImage(math.max(this.bounds.getWidth.toInt,400),math.max(this.bounds.getHeight.toInt,300),BufferedImage.TYPE_INT_ARGB)
  }
  def redrawAll {
    redrawBot
    redrawTop
    redrawMid
    redrawDrawing
  }
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
  def redrawMid{
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getMid)
    model.getMid.getStrokes(false).foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    midImg = img
  }
  def redrawTop{
    val img = createImg
    val g = img.createGraphics()
    //LineDrawer.redraw(img, model.getTop)
    model.getTop.flatMap { x => x.getStrokes(true) }.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    topImg = img
  }
  def redrawDrawing{
    val img = createImg
    val g = img.createGraphics()
    model.getDrawing.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,getZoom,offset,true)
    }
    drawImg = img
  }
  
  def redrawLayerUp{
    val g = botImg.createGraphics
    g.drawImage(midImg, 0, 0, null)
    redrawTop
    redrawMid
  }
  def redrawLayerDown{
    val g = midImg.createGraphics
    g.drawImage(topImg, 0, 0, null)
    topImg = midImg
    redrawMid
    redrawBot
  }
  
  def redrawMergeDown{
    val img = midImg
    redrawMid
    val g = midImg.createGraphics()
    g.drawImage(img, 0, 0, null)
    redrawBot
  }
  
  def redrawLast{
    val g = drawImg.createGraphics()
    model.getLast.foreach{dp=>
      LineDrawer.drawDoodlePartLast(g,dp,getZoom,offset,true)
    }
  }
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
  def prepareMove(coord:Coord){
    tmp = coord
  }
  def move(coord:Coord) = {
    val moved = point+(tmp-coord)/getZoom
    point = Coord(max(min(moved.x,Magic.x),0),max(min(moved.y,Magic.y),0))
    tmp = coord
    //???
  }
  def fullScreen {
    this.publish(new FullscreenEvent)
  }
  def pickColor(x:Int,y:Int,mods:Int)={
    //Todo some mod for picking color from whole image
    if(x>0&&y>0 && x<this.midImg.getWidth&&y<this.midImg.getHeight)
    Some(new Color(this.midImg.getRGB(x, y),true)) else None
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
  def exportImage{
    val img = new BufferedImage((8*magic.x).toInt,(8*magic.y).toInt, BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    model.getLayers.foreach{
      lay=>
        lay.getStrokes(true).foreach { 
          stro =>
            LineDrawer.drawDoodlePart(g,stro,8,Coord(0,0),true)
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
