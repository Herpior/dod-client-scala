package dmodel.tools

import dmodel._
import dmodel.dpart._
import view.DoodlePanel

class EraseLineTool extends SelectTool {

  override def onMouseUp(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(dp, coord, button, control, alt, shift)
    // don't do anything if both are false
    selected match {
      case Some(line:DoodlePart)=>
        val empty = new EmptyLine
        swap(dp, line, empty)
      case None =>
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

}