package dmodel.tools


object DrawTool extends BasicTool {
  
  def getLast = {
    multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
  }

  
  //---------\\
  def startLine(place:Coord,mods:Int){
    val stroke = new MultiLine
    LineTool.startLine(stroke, ColorModel.getColor, SizeModel.getSize, place, mods)
    multiLine = Some(stroke)
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
  /*def addLinePoint{
    multiLine.foreach{
      next =>
        LineTool.addLinePoint(next)
    }
  }*/
  def stopLine(place:Coord,mods:Int){
    multiLine.foreach{
      next =>
        //if(mods/128%2==0)LineTool.addLine(next,tools.getColor, tools.getSize,place,mods)
        //else next.getLast.foreach(last =>LineTool.dragLine(last,place,mods))
        next.compress
        this.layers.getCurrent.add(next)
    }
    multiLine = None
  }
  
  
}