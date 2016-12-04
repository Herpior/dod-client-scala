package view

import scala.swing.Action
import scala.swing.Button
import scala.swing.CheckBox
import scala.swing.Dimension
import scala.swing.FlowPanel
//import java.awt.BasicStroke
import java.awt.Color
//import java.awt.Graphics2D
//import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
//import io.Icons

class SubmitPanel extends FlowPanel/*(Orientation.Vertical)*/ {

  val model = ToolModel
  model.initReady
  
  //this.preferredSize = new Dimension(250, 200)
  //this.minimumSize = preferredSize
  //this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  val submitButt = new Button("submit"){
    this.font = Magic.font20
    this.preferredSize = new Dimension(200, 50)
    //this.margin = new Insets(0, 50, 0, 50)
    //this.border = Swing.EmptyBorder(0, 50, 0, 50)
    this.background = Color.LIGHT_GRAY//Magic.buttColor
    this.foreground = Magic.white
    this.opaque = true
    this.borderPainted = false
    this.action = new Action("Submit"){
      def apply()=submit
    }
  }
  val readyButt = new CheckBox("Ready to submit"){
    this.font = Magic.font20
    this.preferredSize = new Dimension(200, 50)
    this.background = Magic.bgColor
    this.foreground = Magic.white
    //this.selectedIcon = Icons.getCheckIcon
    this.opaque = true
    this.borderPainted = false
    this.action = new Action("Ready to submit"){
      def apply()=clickReady
    }
  }
  this.contents +=   submitButt
  this.contents +=   readyButt
  
  /*
  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    var offy = 0
    /*
    if(model.isReady) g.setColor(Magic.buttColor)
    else g.setColor(Color.LIGHT_GRAY)
    g.fillRoundRect(25, offy, 200, 50, 15, 15)
    g.setColor(Magic.white)
    g.setFont(Magic.font20)
    g.drawString("Submit!", 90, offy+35)
    */
    offy += 100
    
    g.setStroke(new BasicStroke(2))
    g.drawRoundRect(25, offy, 30, 30, 4, 4)
    if(model.isReady) g.drawImage(Icons.getCheck,30,offy+5,null)
    g.setFont(Magic.font20.deriveFont(java.awt.Font.BOLD,18))
    g.drawString("Ready to submit", 70, offy+20)
  }*/
  
  //this.listenTo(mouse.clicks)
  /*
  this.reactions += {
    case e:MouseReleased =>
      val x = e.point.getX.toInt-25
      val y = e.point.getY.toInt
      //println("submit "+x+" "+y)
      /*
      if(y<70 && x<200 && x>0){
      }
      else */if(y>=100 && y<130 && x<200 && x>0){
      }
  }*/
  private def submit = {
    publish(new controller.SubmitEvent)
  }
  private def clickReady = {
    model.clickReady
    submitButt.background = if(model.isReady) Magic.buttColor else Color.LIGHT_GRAY//this.repaint()
  }
  
}