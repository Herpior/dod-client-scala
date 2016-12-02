
package view

import scala.swing.Panel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
import io.Icons

class ToolPickerPanel extends Panel {
  
  val model = ToolModel
  this.preferredSize = new Dimension(250, 100)
  this.minimumSize = preferredSize
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    var offy = 0
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
  
    g.setColor(Magic.buttColor)
    g.setStroke(new BasicStroke(2))
    for(i<-0 to 7){
      g.fillRoundRect(28+i*200/4-i/4*200, offy+i/4*50, 44, 44, 4, 4)
    }
    g.setColor(Magic.white)
    val state = model.getState
    //println("srtat: "+state)
    g.drawRoundRect(28+state*200/4-state/4*200, offy+state/4*50, 44, 44, 4, 4)
    g.drawImage(Icons.getPen,24,offy-3,null)
    g.drawImage(Icons.getLine,75,offy-2,null)
    g.drawImage(Icons.getBez,125,offy-2,null)
    g.drawImage(Icons.getFill,175,offy-2,null)
    g.drawImage(Icons.getPers,25,offy+48,null)
    g.drawImage(Icons.getBezFill,125,offy+48,null)
    g.setColor(Magic.buttColor)
  }
  
  this.listenTo(mouse.clicks)
  
  this.reactions += {
    case e:MouseReleased =>
      val x = e.point.getX.toInt-25+3
      val y = e.point.getY.toInt
      if(x>0&&x<200){
        model.tool( x/50+y/50*4 )
        publish(new controller.ToolChangeEvent(model.getState))
        repaint()
      }
  }

}