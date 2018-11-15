package view

import dmodel.tools.BasicTool
import dmodel.Magic

import scala.swing.{BoxPanel, Dimension, Orientation}

/*
class ConfigPanel(tool:BasicTool) extends BoxPanel(Orientation.Vertical){

  val options = tool.getConfigNames
  val setters = tool.getConfigSetters

  this.preferredSize = new Dimension(200, 50*options.length)
  this.background = Magic.bgColor

  for(setter <- setters) {
    this.contents += new ConfigSlider(setter)
  }
  }

  this.contents += new BoxPanel(Orientation.Vertical){this.preferredSize = new Dimension(200, 5)}

  this.listenTo(grid)
  this.listenTo(slider)
  grid.checkNewSize

  override def paintComponent(g:java.awt.Graphics2D){
    grid.checkNewSize
    super.paintComponent(g)
  }

  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e:controller.SizeChangeEvent =>
      //grid.checkNewSize
      //slider.repaint()
      this.repaint
  }
*/