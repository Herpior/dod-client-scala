package view

import scala.swing._
import scala.swing.event.MouseReleased
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.LayerList

class LayerToolPanel(model:LayerList) extends Panel{
  this.preferredSize = new Dimension(150, 20)
  this.preferredSize = new Dimension(150, 25)
  this.maximumSize = this.preferredSize
  this.background = Magic.bgColor
  
  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Magic.bgColor)
    g.fillRect(0, 0, this.bounds.getWidth.toInt, this.bounds.getHeight.toInt)
    g.setColor(Magic.buttColor)
    g.fillRoundRect(2, 2, 36, 18, 4, 4)
    g.fillRoundRect(42, 2, 42, 18, 4, 4)
    g.fillRoundRect(88, 2, 50, 18, 4, 4)
    g.setColor(Magic.white)
    g.setFont(g.getFont.deriveFont(java.awt.Font.BOLD))
    g.drawString("add", 10, 15)
    g.drawString("merge", 45, 15)
    g.drawString("remove", 92, 15)//TODO undo and redo butts
  }
  this.listenTo(mouse.clicks)
  this.reactions += {
    case e:MouseReleased =>
        val x = e.point.getX
        if(x>=0&&x<40){
          //println("layerlist curr"+model.ind)
          this.model.addLayer
          publish(new RepaintEvent)
          //println("layerlist curr"+model.ind)
        }else if(x>=40&&x<86){
          this.model.mergeLayer
          publish(new RepaintEvent)
        }else if(x>=86&&x<134){
          this.model.removeLayer
          publish(new RepaintEvent)
        }
  }
}