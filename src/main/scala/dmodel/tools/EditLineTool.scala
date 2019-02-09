package dmodel.tools

/**
  * A tool that can be used to edit the size or colour of lines

  * @author Qazhax
  */

import dmodel._
import dmodel.dpart._

class EditLineTool extends SelectTool { //(Array())
  protected var changeColour = true
  protected var changeSize = false

  override def onMouseUp(db: DoodleBufferer, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    //super.onMouseUp(db, coord, button, control, alt, shift)
    // don't do anything if both are false
    if(changeColour||changeSize) selected match {
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
        swap(db, line, edited)
      case Some(line:BasicLine)=>
        val colour = if(changeColour) ColorModel.getColor else line.color
        val size = if(changeSize) SizeModel.getSize else line.size
        val edited = new BasicLine(colour, size)
        edited.setCoords(line.getCoords)
        swap(db, line, edited)
      case Some(line:BezierLine)=>
        val colour = if(changeColour) ColorModel.getColor else line.color
        val size = if(changeSize) SizeModel.getSize else line.size
        val edited = new BezierLine(colour, size)
        edited.setCoords(line.getCoords)
        swap(db, line, edited)
      case None =>
      case any => println(any)
    }
    selected = None
  }

  def swap(db:DoodleBufferer, line:DoodlePart, edited:DoodlePart): Unit ={

    db.model.layers.getCurrent.swap(line, edited)
    val editLine = new EditLine(edited, line)
    db.model.layers.getCurrent.add(editLine)
    db.redrawMid
    db.redrawDrawing
    //db.repaint()
  }

  override def getConfigVariables(): Vector[ConfigVariable] = {
    val colorConfig = new BooleanConfigVariable("set color", _=>this.changeColour, changeColour=_)
    val sizeConfig = new BooleanConfigVariable("set size", _=>this.changeSize, changeSize=_)
    Vector(colorConfig, sizeConfig).asInstanceOf[Vector[ConfigVariable]]
  }

}