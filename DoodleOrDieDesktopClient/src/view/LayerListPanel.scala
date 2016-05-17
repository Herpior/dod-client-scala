package view

/**
 * @author Herpior
 */

import scala.swing._
import dmodel.LayerList
import dmodel.Magic
import dmodel.LineDrawer
import java.awt.BasicStroke
import io.Icons
import java.awt.RenderingHints
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global

class LayerListPanel(model:LayerList) extends Panel{
  
  private var current = model.getCurrent
  this.minimumSize = new Dimension(150, 50+model.toArray.length*(5+Magic.thumbY))
  this.preferredSize = this.minimumSize
  this.maximumSize = new Dimension(200, 50+model.toArray.length*(5+Magic.thumbY))
  
  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    var offy = 20
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
    val offx = Magic.thumbX+10
    g.setStroke(new BasicStroke(3))
    model.toArray.reverse.foreach{
      layer=>
        g.setColor(Magic.red)
        val img = LineDrawer.thumb(layer)
        g.drawImage(img, 5, 5+offy, null)
        if(layer == current)g.drawRect(4, 4+offy, Magic.thumbX+2, Magic.thumbY+2)
        //g.setStroke(new BasicStroke(1))
        g.setColor(Magic.buttColor)
        g.fillRect(offx, 5+offy, 20, 20)
        g.fillRect(offx, 30+offy, 20, 20)
        g.setColor(Magic.white)
        g.drawString("visible", offx+25, 20+offy)
        if(layer.isVisible)g.drawImage(Icons.getVisible,offx,5+offy,null)
        g.drawString("selected", offx+25, 45+offy)
        if(layer.isSelected)g.drawImage(Icons.getCheck,offx,30+offy,null)
        offy += Magic.thumbY+5
    }
  }
  
  def reset = {
    val ht = 50+model.toArray.length*(5+Magic.thumbY)
  this.minimumSize = new Dimension(150, ht)
  this.preferredSize = this.minimumSize
  this.maximumSize = new Dimension(200, ht)
    current = model.getCurrent
    repaint
  }
  
  private var last = 0
  
  this.listenTo(this.mouse.clicks)
  this.reactions += {
    case e:event.MousePressed=>
      val y=e.point.getY-20
      val ht = (Magic.thumbY+5)
      val index = model.size-1-y.toInt/ht
      last = index
    case e:event.MouseReleased=>
      val y=e.point.getY-20
      if(y<0 && y> -20){
        val x = e.point.getX
        if(x>=0&&x<40){
          //println("layerlist curr"+model.ind)
          this.model.addLayer
          publish(new RepaintEvent)
          //println("layerlist curr"+model.ind)
          Future(this.reset)
        }else if(x>=40&&x<86){
          this.model.mergeLayer
          publish(new RepaintEvent)
          Future(this.reset)
        }else if(x>=86&&x<134){
          this.model.removeLayer
          publish(new RepaintEvent)
          Future(this.reset)
        }
      }
      else{
        //println("y: "+y)
        val ht = (Magic.thumbY+5)
        val index = model.size-1-y.toInt/ht
        //println("index: "+index)
        if(index>=0){
          val real = y-(y.toInt/ht)*ht-2
          //println("real: "+real)
          e.point.getX match {
            case x if x<Magic.thumbX=>
              model.switch(index, last)
              model.setCurrent(index)
              publish(new RepaintEvent)
              reset
            case x if x>Magic.thumbX+10 && x<Magic.thumbX+30 =>
              if(real>5&&real<25){
                model.toArray(index).visibility
                publish(new RepaintEvent)
                repaint
              }
              if(real>30&&real<50){
               model.toArray(index).select
               repaint
              }
            case x=>
          }
        }
      }
  }
  
  
}