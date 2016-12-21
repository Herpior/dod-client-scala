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
  this.minimumSize = new Dimension(150, model.toArray.length*(5+Magic.thumbY))
  this.preferredSize = this.minimumSize
  this.maximumSize = new Dimension(200, model.toArray.length*(5+Magic.thumbY))
  
  this.background = Magic.bgColor
  controller.Timer(100,false)(reset).start //might fail if loading the save file takes too long?
  
  private var thumbs = model.toArray.map { layer => (layer, LineDrawer.thumb(layer)) }.toMap
  
  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    var offy = 0
    val offx = Magic.thumbX+10
    g.setStroke(new BasicStroke(3))
    model.toArray.indices.reverse.foreach{
      i=>
        val layer = model.toArray(i)
        val img = thumbs.getOrElse(layer, LineDrawer.thumb(layer))
        g.setColor(Magic.red)
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
    val layers = model.toArray
    val ht = layers.length*(5+Magic.thumbY)
    this.current = model.getCurrent
    this.thumbs = layers.map { 
      layer => 
        val thumb = if(layer!=this.current) thumbs.getOrElse(layer, LineDrawer.thumb(layer)) else LineDrawer.thumb(layer)
        (layer, thumb) 
      }.toMap
    this.minimumSize = new Dimension(150, ht)
    this.preferredSize = this.minimumSize
    this.maximumSize = new Dimension(200, ht)
    this.revalidate()
    this.repaint
  }
  
  private var last = 0
  
  this.listenTo(this.mouse.clicks)
  this.reactions += {
    case e:event.MousePressed=>
      val y=e.point.getY
      val ht = (Magic.thumbY+5)
      val index = model.size-1-y.toInt/ht
      last = index
    case e:event.MouseReleased=>
      val y=e.point.getY
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
          case x if x>Magic.thumbX+10 && x<Magic.thumbX+30 =>
            if(real>5&&real<25){
              model.toArray(index).visibility
              publish(new RepaintEvent)
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