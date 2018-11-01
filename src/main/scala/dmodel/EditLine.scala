package dmodel

class EditLine(editedLine:DoodlePart, originalLine:DoodlePart) extends DoodlePart {
  def getLines: Array[BasicLine] = Array()

  def toJson: JsonStroke = ???
  def toJsonString:String = ""
  def toShortJsonString: String = ""

  def distFrom(point:Coord):Double = Double.MaxValue
  def transform(transformation:Coord=>Coord):DoodlePart = ??? // transform all the edited lines?
  def selection:DoodlePart = this
  override def onUndo(layer:Layer): Unit ={
    layer.swap(editedLine, originalLine)
  }
  override def onRedo(layer:Layer): Unit ={
    layer.swap(originalLine, editedLine)
  }

}
