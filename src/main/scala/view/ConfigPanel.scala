package view

import dmodel.tools._
import dmodel.Magic

import scala.collection.mutable
import scala.swing.{Action, BoxPanel, Dimension, Label, Orientation, RadioButton}


class ConfigPanel(tool:BasicTool) extends BoxPanel(Orientation.Vertical) {

  val configs = tool.getConfigVariables()
  val configContentsThatLikeToStealFocus = mutable.Buffer[swing.Component]()

  this.preferredSize = new Dimension(200, 50 * configs.length)
  this.background = Magic.bgColor

  for (config <- configs) {
    /*
    val label = new Label(config.getName){
      this.foreground = Magic.white
      this.background = Magic.buttColor
    }*/
    /*
    val labelbox = new BoxPanel(Orientation.Horizontal){
      this.contents += label
      this.background = Magic.buttColor
    }*/
    //this.contents += labelbox
    config match {
      case intConf: IntConfigVariable =>
        println("intConf not implemented")
        /*val slider = new ConfigSlider(intConf.asInstanceOf[NumConfigVariable[Int]])
        this.listenTo(slider)
        this.contents += slider*/
      case doubleConf: DoubleConfigVariable =>
        val slider = new ConfigSlider(doubleConf)
        this.listenTo(slider)
        this.contents += slider
      //case stringConf: ConfigVariable[String] =>
      //this.contents += new ConfigSlider[String](stringConf)
      case boolConf: BooleanConfigVariable =>
        val radio = new DodCheckBox(boolConf.getName, _=>boolConf.getVal, boolConf.setVal)
        this.contents += new BoxPanel(Orientation.Horizontal){
          this.contents += radio
          this.background = Magic.buttColor
        }
        configContentsThatLikeToStealFocus += radio
      case unitConf:UnitConfigVariable => // Unit means setter is a function that needs no arguments, but has some side effects
        val button = new DodButton{
          this.action=Action(config.getName){
            unitConf.setVal
          }
        }
        this.contents +=  new BoxPanel(Orientation.Horizontal){
          this.contents += button
        }
        configContentsThatLikeToStealFocus += button
      case x => //ignore unknown formats for now
        println(x.getClass)
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