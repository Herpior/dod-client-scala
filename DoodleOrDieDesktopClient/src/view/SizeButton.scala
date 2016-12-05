package view

import scala.swing.Insets
import scala.swing.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic

class SizeButton extends DodButton {
  
  this.preferredSize = new Dimension(50, 50)
  this.background = Magic.buttColor
  this.foreground = Magic.white
  //this.font = this.font.deriveFont(6)
  this.margin = new Insets(0,0,0,0)
    
  this.borderPainted = false
  this.contentAreaFilled = false
  this.focusPainted = false
  this.opaque = false
  
  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(this.background)
    g.fillRoundRect(3, 3, 44, 44, 44, 44)
    super.paintComponent(g)
  }
  def select {
    this.background = Magic.white
    this.foreground = Magic.buttColor
  }
  def unselect {
    this.background = Magic.buttColor
    this.foreground = Magic.white
  }
}