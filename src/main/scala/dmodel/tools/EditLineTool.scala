package dmodel.tools

/**
  * A tool that can be used to edit the size or colour of lines

  * @author Qazhax
  */

import java.awt.Color

import dmodel._
import dmodel.dpart._

class EditLineTool extends SelectTool { //(Array())
  protected var changeColour = true
  protected var changeSize = false

  override def onMouseUp(db: DoodleBufferer, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    //super.onMouseUp(db, coord, button, control, alt, shift)
    // don't do anything if both are false
    if(changeColour||changeSize) {
      if (control){
        val colour = getColourFromDP(selected)
        if(colour.isDefined) {
          val editedlines = db.model.layers.getCurrent.getStrokes.flatMap(s=>editOneLine(db,Some(s),colour))
          if(editedlines.length != 0) swap(db, editedlines)
        }
      }
        else{
        val edited = editOneLine(db, selected)
        edited.foreach(el=>swap(db, el._1, el._2))
      }
    }
    selected = None
  }

  def getColourFromDP(doodlepart:Option[DoodlePart]) ={
    doodlepart match {
      case Some(line:BasicLine)=>Some(line.color)
      case Some(line:BezierLine)=>Some(line.color)
      case _ => None
    }
  }

  /**
    * if filterColour = None, edit line normally. if Some, edit line only if it's colour is the same colour
    * */
  def editOneLine(db:DoodleBufferer, doodlepart:Option[DoodlePart], filterColour:Option[Color]=None):Option[(DoodlePart,DoodlePart)] = {
    doodlepart match {
      case Some(line:MultiLine)=>
        val edited = new MultiLine
        var found = false
        val lines = line.getLines.map{
          subline=>
            if(filterColour.isEmpty || filterColour.get == subline.color) {
              found = true
              editBasicLine(subline)
            }
            else subline
        }
        if(found){
          edited.setLines(lines)
          Some(line, edited)
        }
        else None
      case Some(line:BasicLine)=>
        if(filterColour.isEmpty || filterColour.get == line.color){
          val edited = editBasicLine(line)
          Some(line, edited)
        }
        else None
      case Some(line:BezierLine)=>
        if(filterColour.isEmpty || filterColour.get == line.color){
          val edited = editBezierLine(line)
          Some(line, edited)
        }
        else None
      case Some(_:EmptyLine)=>None
      case None => None
      case any =>
        println(any)
        None
    }
  }

  def editBasicLine(line:BasicLine) ={
    val colour = if(changeColour) ColorModel.getColor else line.color
    val size = if(changeSize) SizeModel.getSize else line.size
    val editedLine = new BasicLine(colour, size)
    editedLine.setCoords(line.getCoords)
    editedLine
  }
  def editBezierLine(line:BezierLine) ={
    val colour = if(changeColour) ColorModel.getColor else line.color
    val size = if(changeSize) SizeModel.getSize else line.size
    val editedLine = new BezierLine(colour, size)
    editedLine.setCoords(line.getCoords)
    editedLine
  }

  def swap(db:DoodleBufferer, line:DoodlePart, edited:DoodlePart): Unit ={

    val editLine = swapLine(db, line, edited)
    db.model.layers.getCurrent.add(editLine)
    db.redrawMid
    db.redrawDrawing
    //db.repaint()
  }
  def swap(db:DoodleBufferer, edits:Array[(DoodlePart,DoodlePart)]): Unit ={
    val editlines = edits.map(el=>swapLine(db, el._1, el._2))
    val editLine = new MultiEditLine(editlines)
    db.model.layers.getCurrent.add(editLine)
    db.redrawMid
    db.redrawDrawing
    //db.repaint()
  }
  def swapLine(db:DoodleBufferer, line:DoodlePart, edited:DoodlePart) ={

    db.model.layers.getCurrent.swap(line, edited)
    new EditLine(edited, line)
  }

  override def getConfigVariables(): Vector[ConfigVariable] = {
    val colorConfig = new BooleanConfigVariable("set color", _=>this.changeColour, changeColour=_)
    val sizeConfig = new BooleanConfigVariable("set size", _=>this.changeSize, changeSize=_)
    Vector(colorConfig, sizeConfig).asInstanceOf[Vector[ConfigVariable]]
  }

}