package view

import scala.swing.Dimension
import scala.swing.Insets
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic

class LayerButton extends DodButton {
  
  this.preferredSize = new Dimension(50, 20)
  this.borderPainted = false
  //this.font = this.font.deriveFont(8f);
  //this.background = Magic.buttColor
  this.margin = new Insets(-5,-5,-5,-5)
  
  this.contentAreaFilled = false
  this.focusPainted = false
  this.opaque = false

  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Magic.buttColor)
    g.fillRoundRect(2, 2, 46, 18, 8, 8)
    g.setColor(Magic.white)
    super.paintComponent(g)
  }
}