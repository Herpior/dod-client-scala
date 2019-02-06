package view

import scala.swing._
import java.awt.{Dialog => _, Dimension => _, Graphics2D => _, Panel => _, _}
import java.awt.image.BufferedImage
import java.io.File

import controller.FullscreenEvent
import dmodel._
import dmodel.dpart.DoodlePart

import scala.swing.event.UIElementResized
//import java.awt.Cursor
import math.{max,min}

class DoodlePanel extends Panel {
  def getWidth: Int = this.bounds.getWidth.toInt
  def getHeight: Int = this.bounds.getHeight.toInt

  val pencil: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(io.Icons.getPenCursor, new java.awt.Point(0,0), "pencil")
  this.cursor = pencil
  this.minimumSize = new Dimension(400,300)
  this.preferredSize = new Dimension(2*Magic.x,2*Magic.y)
  this.maximumSize = new Dimension(4320,3240)
  val model = new DoodleModel
  val bufferer = new DoodleBufferer(model, this.getWidth, this.getHeight)

  private var cursorX = -100
  private var cursorY = -100

  override def repaint: Unit = {
    if(this.bufferer.dirty){
      this.bufferer.redrawAll
    }
    super.repaint()
  }
  
  override def paintComponent(g:Graphics2D){
    //this.bufferer.setBounds(this.getWidth, this.getHeight) //check if there is a better spot for this, like on resized
    val zoom = this.bufferer.getZoom
    val tmp = this.bufferer.getTmp
    val off = this.bufferer.offset
    g.setColor(Magic.bgColor)//(Magic.bgColor)
    g.fillRect(0, 0, getWidth, getHeight)
    
    val gx = -(tmp.x*zoom).toInt
    val gy = -(tmp.y*zoom).toInt
    //this.super.paintComponent(g)
    g.drawImage(this.bufferer.botImg,gx,gy,null)
    g.drawImage(this.bufferer.midImg,gx,gy,null)
    g.drawImage(this.bufferer.drawImg,gx,gy,null)
    g.drawImage(this.bufferer.topImg,gx,gy,null)
    g.setColor(Magic.bgColorAlpha)
    val canvas = Magic.doodleSize * zoom
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
    val col_opt = this.bufferer.pickColor(cursorX, cursorY, true)
    var col = Magic.black
    col_opt.foreach { x => col = dmodel.Colors.inverse(x)}
    
    //val invert = dmodel.Colors.inverse(col)
    g.setColor(col)
    val bsize = max(2, SizeModel.getSize*zoom).toInt
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
  def setCursor(x:Double,y:Double): Unit ={
    cursorX = x.toInt
    cursorY = y.toInt
  }/*
  def move1(coord:Coord) = {//deprecated, use hand tool
    val moved = point+(tmp-coord)/getZoom
    point = Coord(max(min(moved.x,Magic.x),0),max(min(moved.y,Magic.y),0))
    tmp = Coord(0,0)
    //???
  }*/
  /*
  def fullScreen {
    this.publish(new FullscreenEvent)
  }*/


  def exportImage(percent:Double, path:String){
    val img = new BufferedImage((percent*Magic.x).toInt,(percent*Magic.y).toInt, BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    model.getLayers.foreach{
      lay=>
        lay.getVisibleStrokes(true).foreach {
          stro =>
            LineDrawer.drawDoodlePart(g,stro,percent,Coord(0,0),true)
        }
    }
    //var out: java.io.OutputStream = null
    try{
      //val localfile = "sample2.png"
      //out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(localfile))
      //out.write(img.)
      //val path = Dialog.showInput(this, "save location", "save", Dialog.Message.Question, null, List(), "exported.png")
      //path.foreach{
      //p=>
      val outputfile = new File(path)
      val check = if(outputfile.exists())Dialog.showConfirmation(this, "this file already exists, overwrite?", "overwrite?", Dialog.Options.YesNo, Dialog.Message.Question, null)==Dialog.Result.Yes else true
      if(check)javax.imageio.ImageIO.write(img, "png", outputfile)
      //}
    } catch {
      case e:Throwable => e.printStackTrace()
    } finally {
      //out.close
    }
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

  this.reactions += {
    case e:UIElementResized =>
      this.bufferer.setBounds(this.getWidth, this.getHeight)
      this.repaint
  }

}
