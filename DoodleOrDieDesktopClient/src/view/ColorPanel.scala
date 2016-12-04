package view

import scala.swing.Action
import scala.swing.Swing
import scala.swing.Button
import scala.swing.GridPanel
import scala.swing.Dimension
import scala.swing.event._
//import java.awt.BasicStroke
import java.awt.Color
import javax.swing.BorderFactory
import java.awt.Graphics2D
//import java.awt.RenderingHints
import dmodel.Colors
import dmodel.Coord
import dmodel.Magic
import dmodel.ColorModel
//import javafx.scene.control.ColorPicker

class ColorPanel extends GridPanel(ColorModel.rows, ColorModel.rowl) {
  
  val ht = math.max(this.rows*12, 80)
  
  val model = ColorModel
  this.preferredSize = new Dimension(200, ht)
  this.minimumSize = new Dimension(200, ht)
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  
  private val bordero = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 1), BorderFactory.createLineBorder(Color.BLACK, 1))
  private val colors = model.getColors
  val buttons = Array.ofDim[Button](colors.length)
  for (i <- colors.indices){
    buttons(i) = new Button(){
      this.opaque = true
      this.background = colors(i)
      this.border = bordero
      this.borderPainted = false 
      override def paintComponent(g:Graphics2D){
        //if color is not opaque, draw checkerboards
        if(this.background.getAlpha<255){
          g.setColor(Color.WHITE)
          val halfw = (this.bounds.getWidth*0.5).toInt
          val halfh = (this.bounds.getHeight*0.5).toInt
          g.fillRect(0, 0, 2*halfw, 2*halfh)
          g.setColor(Color.BLACK)
          g.fillRect(0, halfh, halfw, halfh)
          g.fillRect(halfw, 0, halfw, halfh)
        }
        super.paintComponent(g)
      }
      this.listenTo(mouse.clicks)
      this.reactions += {
        case e:MouseReleased=>
          val mods = e.modifiers
          if(e.modifiers/256%2==1){
            selectSecondary(i)
          }
          else {
            selectPrimary(i)
          }
        case e:MouseClicked =>
          if(e.clicks>1 && e.modifiers == 0 && Magic.authorized){
            openPicker(i)
          }
      }
    }
  }
  this.contents ++= buttons
  
  def selectPrimary(i:Int){
    model.primaryColor(i)
    buttons.foreach { x => x.borderPainted = false }
    buttons(i).borderPainted = true
  }
  def selectSecondary(i:Int){
    model.secondaryColor(i)
    buttons.foreach { x => x.borderPainted = false }
    buttons(i).borderPainted = true
  }
  def openPicker(i:Int){
    val dres = swing.ColorChooser.showDialog(this,"pick a color",model.getColor)//(0))
    //Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
    dres.foreach{
      colr => model.setColor(colr)
      buttons(i).background = model.getColor
    }
    //this.repaint()
  
  }
  
  selectPrimary(0)
  /*
  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
  
    var offy = 0
    val wid = 230/model.rowl
    val hei = 120/model.rows
    val inter = hei/20
    val inter2 = inter/2
    val colors = model.getColors
    var currx = 0
    var curry = 0
    var old = Magic.white
    for(i<-colors.indices){
      g.setColor(colors(i))
      val x = 10+(i%model.rowl)*wid+inter2
      val y = offy+(i/model.rowl)*hei+inter2
      g.fillRoundRect(x, y, wid-inter, hei-inter, wid/4, wid/4)
      if(i == model.colorIndex){
        currx = x
        curry = y
        old = g.getColor
      }
    }
    val c = Colors.inverse(old)
    val c2 = if(old.getRed+old.getBlue+old.getGreen>255*1.5) Color.BLACK else Color.WHITE
    g.setColor(c2)
    g.setStroke(new BasicStroke(1))
    g.drawRoundRect(currx+1, curry+1, wid-inter-2, hei-inter-2, wid/4, wid/4)
    g.setColor(c)
    g.drawRoundRect(currx, curry, wid-inter, hei-inter, wid/4, wid/4)
  }

  this.listenTo(mouse.clicks)
  
  this.reactions += {
    case e:MouseClicked =>
      val y = e.point.getY
      if(e.clicks>1 && e.modifiers == 0 && Magic.authorized){
        val dres = swing.ColorChooser.showDialog(this,"pick a color",model.getColor)//(0))
        //Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
        dres.foreach(col => model.setColor(col))
        this.repaint()
      }
    case e:MouseReleased => 
      val x = e.point.getX.toInt-25
      val y = e.point.getY
      if(x> -20 && x<205){
        //println(e.modifiers+" -> "+e.modifiers/4096%2)
        if(e.modifiers/256%2==1){
          model.secondaryColor(Coord((x+18),(y)),Coord(230,120))
          //println("secondary")
            //publish(new ColorChangeEvent(model.getColor(1),1))
        } else {
          model.primaryColor(  Coord((x+18),(y)),Coord(230,120))
            //publish(new ColorChangeEvent(model.getColor(0),0))
        
          if(e.modifiers/128%2==1 && Magic.authorized){
            val dres = swing.ColorChooser.showDialog(this,"pick a color",model.getColor)//(0))
            //Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
            dres.foreach(col => model.setColor(col))
            this.repaint()
          }
        }
        repaint()
      }
  }*/
}