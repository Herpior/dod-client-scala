package dmodel.tools
import dmodel.MultiLine
import dmodel.Coord
import dmodel.ColorModel
import dmodel.SizeModel

object DrawTool extends LineToolClass {
  
  def getLast = {
    //multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
    multiLine.getLast.flatMap(_.getLastLine)
  }

  
  //---------\\
  /*
  def startLine(place:Coord,mods:Int){
    //val stroke = new MultiLine
    LineTool.startLine(ColorModel.getColor, SizeModel.getSize, place, mods)
    //multiLine = stroke
  }
  def dragLine(place:Coord,mods:Int){
    if(multiLine.isEmpty){
      startLine(place,mods)
    }
    multiLine.foreach(_.getLast.foreach{
      next =>
        LineTool.dragLine(next, place, mods)
    })
  }
  def addLine(place:Coord,mods:Int){
    multiLine.foreach{
      next =>
        LineTool.addLine(next, ColorModel.getColor, SizeModel.getSize, place, mods)
    }
  }
  */
  /*def addLinePoint{
    multiLine.foreach{
      next =>
        LineTool.addLinePoint(next)
    }
  }*/
  def stopLine(place:Coord,mods:Int):MultiLine = {
    multiLine.compress
       // this.layers.getCurrent.add(multiLine)
    
    val returning = multiLine
    multiLine = new MultiLine
    returning
  }
  
  
}