package view

import dmodel.tools.{BasicTool, ConfigVariable}
import dmodel.Magic

import scala.swing.{BoxPanel, Dimension, Label, Orientation}


class ConfigPanel(tool:BasicTool) extends BoxPanel(Orientation.Vertical) {

  val configs = tool.getConfigVariables()

  this.preferredSize = new Dimension(200, 50 * configs.length)
  this.background = Magic.bgColor

  for (config <- configs) {
    val label = new Label(config.getName)
    this.contents += label
    config.getVal match {
      case o: Int =>
        val slider = new ConfigSlider[Int](config.asInstanceOf[ConfigVariable[Int]])
        this.listenTo(slider)
        this.contents += slider
      case o: Double =>
        val slider = new ConfigSlider[Double](config.asInstanceOf[ConfigVariable[Double]])
        this.listenTo(slider)
        this.contents += slider
      case o: String =>
      //this.contents += new ConfigSlider[String](option.asInstanceOf[ConfigVariable[String]])
      case _ => //ignore unknown formats for now
    }
  }

  this.contents += new BoxPanel(Orientation.Vertical) {
    this.preferredSize = new Dimension(200, 5)
  }

  override def paintComponent(g: java.awt.Graphics2D) {
    super.paintComponent(g)
  }

  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e: controller.SizeChangeEvent =>
      //grid.checkNewSize
      //slider.repaint()
      this.repaint
  }
}