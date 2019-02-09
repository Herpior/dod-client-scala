package dmodel.tools

/**
  * A tool that can be used to erase entire lines

  * @author Qazhax
  */

import dmodel._
import dmodel.dpart._

class EraseLineTool extends EditLineTool {

  changeSize = false
  changeColour = false

  override def onMouseUp(db: DoodleBufferer, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    //super.onMouseUp(db, coord, button, control, alt, shift)
    selected match {
      case Some(line:DoodlePart)=>
        val empty = new EmptyLine
        swap(db, line, empty)
      case None =>
      case any => println(any)
    }
    selected = None
  }

/*
  def swap(db:DoodleBufferer, line:DoodlePart, edited:DoodlePart): Unit ={

    db.model.layers.getCurrent.swap(line, edited)
    val editLine = new EditLine(edited, line)
    db.model.layers.getCurrent.add(editLine)
    db.redrawMid
    db.redrawDrawing
    db.repaint()
  }*/
override def getConfigVariables(): Vector[Nothing] = {Vector()}

}