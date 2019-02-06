
package view

import scala.swing.Action
import scala.swing.GridPanel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
import io.Icons

class ToolPickerPanel extends GridPanel(2, 4) {
  
  val model: ToolModel.type = ToolModel
  this.preferredSize = new Dimension(200, 100)
  this.minimumSize = preferredSize
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  val icons = Array(Icons.getPen, Icons.getLine, Icons.getBez, Icons.getFill, 
                    Icons.getPers, Icons.getZoom, Icons.getDrag, Icons.getBezFill,
                    Icons.getColorInjector, Icons.getEraser)
  val toolcount: Int = icons.length
  val buttons: Array[ToolButton] = Array.ofDim[ToolButton](toolcount)
  for(i<-0 until toolcount){
    buttons(i) = new ToolButton(icons(i)){
      this.action = new Action(""){
        def apply(): Unit = click(i)
      }
    }
  }

  this.contents ++= buttons
  
  def setTool(i:Int){
    val orig = model.getState
    buttons(orig).borderPainted = false
    model.setTool(i)
    val next = model.getState
    buttons(next).borderPainted = true
    publish(new controller.ToolChangeEvent(model.getState))

    buttons(orig).repaint()
    buttons(next).repaint()
  }
  
  def click(i:Int){
    this.setTool(i)
  }
  
  click(0)
  
  /*
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
    g.drawImage(,24,offy-3,null)
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
  }*/

}