package view

import scala.swing.{Action, Button, Dimension, GridPanel, Rectangle, Swing}
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
  
  val ht = math.max(this.rows*12, 80)//+10
  
  val model = ColorModel
  this.preferredSize = new Dimension(200, ht)
  this.minimumSize = new Dimension(200, ht)
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  //this.border = Swing.EmptyBorder(5, 0, 5, 0)
  
  //private val bordero = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 1), BorderFactory.createLineBorder(Color.BLACK, 1))
  private val primaryBorder = javax.swing.BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.cyan), BorderFactory.createLoweredBevelBorder())
  private val secondaryBorder=javax.swing.BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red), BorderFactory.createRaisedBevelBorder())
  private val colors = model.getColors //only used for the button creation loop, possibly outdated later. maybe change something?
  val buttons = Array.ofDim[Button](colors.length)
  for (i <- colors.indices){
    buttons(i) = new Button(){
      this.opaque = true
      this.background = colors(i)
      this.border = primaryBorder
      this.borderPainted = false

      override def paintComponent(g:Graphics2D){
        super.paintComponent(g)
        this.background = ColorModel.getColors(i)
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


  def setColor(color: Color) = {
    model.setColor(color)
    buttons(model.colorIndex).repaint()
  }
  def selectPrimary(i:Int){
    changeColor(_=>model.primaryColor(i))
  }
  def selectSecondary(i:Int){
    changeSecondary(_=>model.secondaryColor(i))
  }
  def colorUp =  {
    changeColor(_=>model.colorUp)
  }
  def colorDown = {
    changeColor(_=>model.colorDown)
  }
  def colorLeft = {
    changeColor(_=>model.colorLeft)
  }
  def colorRight = {
    changeColor(_=>model.colorRight)
  }
  def changeColor(func:Unit=>Unit) = {
    val orig = model.colorIndex
    buttons(orig).borderPainted = false
    func()
    val next = model.colorIndex
    buttons(next).border = primaryBorder
    buttons(next).borderPainted = true

    buttons(orig).repaint()
    buttons(next).repaint()
  }
  def changeSecondary(func:Unit=>Unit) = {
    val orig = model.colorIndex2
    val primary = model.colorIndex
    if(orig != primary) buttons(orig).borderPainted = false
    func()
    val next = model.colorIndex2
    if(next != primary) buttons(next).border = secondaryBorder
    buttons(next).borderPainted = true

    buttons(orig).repaint()
    buttons(next).repaint()
  }
  def openPicker(i:Int){
    val dres = swing.ColorChooser.showDialog(new swing.Button,"pick a color",model.getColor)//(0))
    //Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
    dres.foreach{
      colr => model.setColor(colr)
      buttons(i).background = model.getColor
    }
  }/*
  def resetCurrent{
    buttons(ColorModel.colorIndex).background = ColorModel.getColor
  }
  def reset {
    model.getColors
  }*/
  
  selectPrimary(0)
 
}