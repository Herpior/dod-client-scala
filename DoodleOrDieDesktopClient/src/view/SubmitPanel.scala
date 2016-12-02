package view

import scala.swing.Panel
import scala.swing.Dimension
//import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
import io.Icons

class SubmitPanel extends Panel {

  val model = ToolModel
  this.preferredSize = new Dimension(250, 200)
  this.minimumSize = preferredSize
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    var offy = 0
    
    g.setColor(Magic.buttColor)
    g.fillRoundRect(25, offy, 200, 50, 15, 15)
    g.setColor(Magic.white)
    g.setFont(Magic.font20)
    g.drawString("Submit!", 90, offy+35)
    
    offy += 100
    
    g.drawRoundRect(25, offy, 30, 30, 4, 4)
    if(model.isReady) g.drawImage(Icons.getCheck,30,offy+5,null)
    g.setFont(Magic.font20.deriveFont(java.awt.Font.BOLD,18))
    g.drawString("Ready to submit", 70, offy+20)
  }
  
}