package view

import scala.swing.Panel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.SizeModel
import dmodel.Magic

class SizeSlider extends Panel{
  
  this.minimumSize = new Dimension(200,30)
  this.preferredSize = this.minimumSize
  this.maximumSize = this.minimumSize
  this.background = Magic.bgColor
  
  private val start = 15
  private val length = 170
  private val ballSize = 30
  private def maxLog = Math.log(SizeModel.maxSize)
  
  private var changingSize = false

  
  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    val trueSize = SizeModel.getSize
    val size = xFromSize(trueSize)
    g.setColor(Magic.buttColor)
    g.fillRect(start, 12, length, 6)
    //g.fillOval(155, 155, 40, 40)
    g.setColor(Magic.white)
    g.fillOval(start+size-ballSize/2, 0, ballSize, ballSize)
    g.setColor(Magic.buttColor)
    val offset = size.toInt.toString().length()*3
    g.setFont(g.getFont.deriveFont(java.awt.Font.BOLD))
    g.drawString(""+trueSize, start+size-offset, 19)
    
  }
  
  def sizeFromX(x:Double) = {
    Math.exp(x/length*maxLog).toInt
  }
  def xFromSize(size:Int) = {
    ( length*Math.log(size)/maxLog ).toInt
  }
  
  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  
  this.reactions += {
    case e:MouseDragged =>
      val x = e.point.getX-start
      val size = sizeFromX(x)
      //if(changingSize){
        SizeModel.setSize(size)
        publish(new controller.SizeChangeEvent(SizeModel.getSize))
        //repaint
        //this.nextsize = min(max(x.toInt,1),200)
      //}
    case e:MousePressed =>
      val x = e.point.getX-start
      val size = sizeFromX(x)
      SizeModel.setSize(size)
      publish(new controller.SizeChangeEvent(SizeModel.getSize))
      //repaint
  }
}