package view

import scala.swing.BoxPanel
import scala.swing.Dimension
import scala.swing.event._
import scala.swing.Orientation
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.SizeModel


class SizePanel extends BoxPanel(Orientation.Vertical){
  
  val model = SizeModel
  this.preferredSize = new Dimension(200, 135)
  this.background = Magic.bgColor
  val grid = new SizeGrid
  val slider = new SizeSlider
  this.contents += grid
  this.contents += slider
  
  this.listenTo(grid)
  grid.checkNewSize
  
  
  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e:controller.SizeChangeEvent =>
      grid.checkNewSize
      slider.repaint()
  }

}