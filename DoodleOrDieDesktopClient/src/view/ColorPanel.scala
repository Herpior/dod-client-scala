package view

import scala.swing.Panel
import scala.swing.Dimension
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Colors
import dmodel.Magic
import dmodel.ToolModel

class ColorPanel extends Panel {
  
  val model = ToolModel
  this.preferredSize = new Dimension(250, 130)
  this.minimumSize = new Dimension(200, 130)
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  override def paintComponent(g:Graphics2D){
    //super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
  
    var offy = 0
    val wid = 230/model.rowl
    val hei = 120/model.rows
    val inter = hei/20
    val inter2 = inter/2
    val colors = model.getColors
    var currx = 0
    var curry = 0
    var old = Magic.white
    for(i<-colors.indices){
      g.setColor(colors(i))
      val x = 10+(i%model.rowl)*wid+inter2
      val y = offy+(i/model.rowl)*hei+inter2
      g.fillRoundRect(x, y, wid-inter, hei-inter, wid/4, wid/4)
      if(i == model.colorIndex){
        currx = x
        curry = y
        old = g.getColor
      }
    }
    val c = Colors.inverse(old)
    val c2 = if(old.getRed+old.getBlue+old.getGreen>255*1.5) Color.BLACK else Color.WHITE
    g.setColor(c2)
    g.setStroke(new BasicStroke(1))
    g.drawRoundRect(currx+1, curry+1, wid-inter-2, hei-inter-2, wid/4, wid/4)
    g.setColor(c)
    g.drawRoundRect(currx, curry, wid-inter, hei-inter, wid/4, wid/4)
  }
}