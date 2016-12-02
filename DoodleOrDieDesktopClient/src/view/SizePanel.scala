package view

import scala.swing.Panel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel


class SizePanel/*(model:ToolModel)*/ extends Panel/*BoxPanel(Orientation.Vertical)*/ {
  
  val model = ToolModel
  this.preferredSize = new Dimension(250, 150)
  this.minimumSize = preferredSize
  this.maximumSize = preferredSize
  this.background = Magic.bgColor

  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    var offy = 5
    val buttHalf = 25
    val buttWidth = buttHalf*2
    val b = buttHalf+offy
    val t = 2*buttHalf+b
    g.setColor(Magic.buttColor)
    for(y<-0 to 1){
      for(x<-0 to 3){
        g.fillOval(x*buttWidth+30, y*buttWidth+offy, 40, 40)
      }
    }
    
    g.setColor(Color.WHITE)
    g.setFont(g.getFont.deriveFont(Font.BOLD))
    g.drawString("XXS", 14+buttHalf, b)
    g.drawString("XS", 68+buttHalf, b)
    g.drawString("S", 122+buttHalf, b)
    g.drawString("M", 171+buttHalf, b)
    g.drawString("L", 22+buttHalf, t)
    g.drawString("XL", 69+buttHalf, t)
    g.drawString("XXL", 116+buttHalf, t)
    g.drawString("XXXL", 160+buttHalf, t)
    val sizein = model.sizes.indexOf(model.getSize)
    if(sizein > -1){
      val x = sizein % 4
      val y = sizein / 4
      g.setColor(Color.WHITE)
      g.fillOval(x*50+30, y*50+offy, 40, 40)
      g.setColor(Magic.buttColor)
      sizein match{
        case 0 => g.drawString("XXS", 14+buttHalf, b)
        case 1 => g.drawString("XS", 68+buttHalf, b)
        case 2 => g.drawString("S", 122+buttHalf, b)
        case 3 => g.drawString("M", 171+buttHalf, b)
        case 4 => g.drawString("L", 22+buttHalf, t)
        case 5 => g.drawString("XL", 69+buttHalf, t)
        case 6 => g.drawString("XXL", 116+buttHalf, t)
        case 7 => g.drawString("XXXL", 160+buttHalf, t)
      } 
    } 
    
    offy+=105
    val size = this.model.getSize
    g.setColor(Magic.buttColor)
    g.fillRect(25, offy+12, 200, 6)
    //g.fillOval(155, 155, 40, 40)
    g.setColor(Color.WHITE)
    g.fillOval(size.toInt+10, offy, 30, 30)
    g.setColor(Magic.buttColor)
    val offset = size.toInt.toString().length()*3
    g.drawString(""+size, size+25-offset, offy+19)
    
  }
  
  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    
        case e:MouseDragged =>
          val x = e.point.getX-25
          val y = e.point.getY
          if(model.changingSize){
            model.setSize(x.toInt)
            publish(new controller.SizeChangeEvent(model.getSize))
            //this.nextsize = min(max(x.toInt,1),200)
            repaint
          }
        case e:MousePressed =>
          val x = e.point.getX-25
          val y = e.point.getY
          //println(e.modifiers)
          if(y>110 && y<140){
            model.setSize(x.toInt)
            model.pressSlider
            publish(new controller.SizeChangeEvent(model.getSize))
            repaint
          }
        case e:MouseReleased => 
          val x = e.point.getX.toInt-25
          val y = e.point.getY
          //println(y)
          if(model.changingSize){
            //model.setSize(x.toInt)
            model.releaseSlider
            //publish(new SizeChangeEvent(model.getSize))
            //repaint()
          }
          if(y<50){
              x/50 match {
                case 0 => model.setSize(1)
                case 1 => model.setSize(3)
                case 2 => model.setSize(5)
                case _ => model.setSize(10)
              }
              publish(new controller.SizeChangeEvent(model.getSize))
              repaint()
            }else if(y<110){
              x/50 match {
                case 0 => model.setSize(25)
                case 1 => model.setSize(50)
                case 2 => model.setSize(100)
                case _ => if(Magic.authorized)model.setSize(200)
              }
              publish(new controller.SizeChangeEvent(model.getSize))
              repaint()
            }
  }

}