package view

import scala.swing._
import java.awt.BasicStroke
import java.awt.image.BufferedImage
import java.awt.Color
import dmodel.Magic
import dmodel.Layer
import dmodel.LineDrawer
import dmodel.Coord

class LayerPanel(layer:Layer) extends Panel {

  private var thumb = createImg
  
  
  def redraw{
    val img = createImg
    val g = thumb.createGraphics()
    layer.getStrokes(false).foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,Magic.thumbZoom,Coord(0,0),true)
    }
  } 
  def redrawLast{
    val img = createImg
    val g = thumb.createGraphics()
    layer.getStrokes(false).lastOption.foreach{dp=>
      LineDrawer.drawDoodlePart(g,dp,Magic.thumbZoom,Coord(0,0),true)
    }
  }
  
  def createImg = {
    new BufferedImage(Magic.thumbX,Magic.thumbY,BufferedImage.TYPE_INT_ARGB)
  }
  
  override def paintComponent(g:Graphics2D) {
    
        //TODO fix the thingy -v
        //g.setStroke(new BasicStroke(1))
        g.setColor(Color.WHITE)
        g.fillRect(0, 0, Magic.thumbX, Magic.thumbY)
        g.drawImage(thumb, 0,0,null)
        //g.setColor(Magic.buttColor)
        //g.drawRect(24, 619, 202, 152)
        //g.drawRect(26+(offset.x*200/Magic.x), 621+(offset.y*150/Magic.y), (400/zoom).toInt-2, (300/zoom).toInt-2)
        //g.setColor(Color.WHITE)
        //g.drawRect(25, 620, 200, 150)
        //g.drawRect(27+(offset.x*200/Magic.x), 622+(offset.y*150/Magic.y), (400/zoom).toInt-4, (300/zoom).toInt-4)
        
  }
  
}