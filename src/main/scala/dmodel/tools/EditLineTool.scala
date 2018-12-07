package dmodel.tools

import dmodel._
import dmodel.dpart._
import view.DoodlePanel

class EditLineTool extends SelectTool { //(Array())
  private var changeColour = true
  private var changeSize = false

  override def onMouseUp(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(dp, coord, button, control, alt, shift)
    selected match {
      case Some(line:MultiLine)=>
        val edited = new MultiLine
        val lines = line.getLines.map{
          subline=>
            val colour = if(changeColour) ColorModel.getColor else subline.color
            val size = if(changeSize) SizeModel.getSize else subline.size
            val editedSubline = new BasicLine(colour, size)
            editedSubline.setCoords(subline.getCoords)
            editedSubline
        }
        edited.setLines(lines)
        swap(dp, line, edited)
      case Some(line:BasicLine)=>
        val colour = if(changeColour) ColorModel.getColor else line.color
        val size = if(changeSize) SizeModel.getSize else line.size
        val edited = new BasicLine(colour, size)
        edited.setCoords(line.getCoords)
        swap(dp, line, edited)
      case Some(line:BezierLine)=>
        val colour = if(changeColour) ColorModel.getColor else line.color
        val size = if(changeSize) SizeModel.getSize else line.size
        val edited = new BezierLine(colour, size)
        edited.setCoords(line.getCoords)
        swap(dp, line, edited)
      case any => println(any)
    }
    selected = None
  }

  def swap(dp:DoodlePanel, line:DoodlePart, edited:DoodlePart): Unit ={

    dp.model.layers.getCurrent.swap(line, edited)
    val editLine = new EditLine(edited, line)
    dp.model.layers.getCurrent.add(editLine)
    dp.redrawMid
    dp.redrawDrawing
    dp.repaint()
  }

  override def getConfigVariables() = {
    val colorConfig = new BooleanConfigVariable("set color", _=>this.changeColour, changeColour=_)
    val sizeConfig = new BooleanConfigVariable("set size", _=>this.changeSize, changeSize=_)
    Vector(colorConfig, sizeConfig).asInstanceOf[Vector[ConfigVariable]]
  }

}