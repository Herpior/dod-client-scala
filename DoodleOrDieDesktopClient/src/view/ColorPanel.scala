package view

import scala.swing.Action
import scala.swing.Swing
import scala.swing.Button
import scala.swing.GridPanel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.Color
import javax.swing.BorderFactory
import java.awt.Graphics2D
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
  private val colors = model.getColors //only used for the button creation loop, possibly outdated later. maybe change something?
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
          else if(e.modifiers/128%2==1 && Magic.authorized){
            openPicker(i)
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
  }
  
  selectPrimary(0)
 
}