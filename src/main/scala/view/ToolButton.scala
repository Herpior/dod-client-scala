package view

import scala.swing.Dimension
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import javax.swing.BorderFactory
import dmodel.Magic

class ToolButton(image:Image) extends DodButton {
  
  this.preferredSize = new Dimension(50, 50)
  this.border = BorderFactory.createLineBorder(Magic.white, 4)
  this.borderPainted = false
  //this.background = Magic.buttColor
  
  this.contentAreaFilled = false
  this.focusPainted = false
  this.opaque = false

  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Magic.buttColor)
    g.fillRoundRect(3, 3, 44, 44, 8, 8)
    g.drawImage(image,0,0,null)
  }
}