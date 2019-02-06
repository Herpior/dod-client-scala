package dmodel.dpart
import dmodel.{Coord, JsonStroke}

/* Class for deleted lines, or lines that don't have any inherent line-ness, but that are necessary in the undo stack
 * used as an anchor in the line stack for undoing the deletion.
 * should only equal itself and no other deletedlines.
 * not saved onto disk, as other programs don't seem to record deletions in saves either,
 * and it would be more difficult to keep track of the deleted lines in saves .
 * (an unique identifier might work, but it'd need to stay unique across multiple save and reload loops and stored)
 */
class EmptyLine extends DoodlePart {
  def distFrom(point:Coord): Double = Double.MaxValue // return maxValue so the line can't be selected accidentally

  // these lines are not exactly lines that could be drawn, transformed, selected, or saved, as told before
  override def getLines = Array()
  override def transform(transformation: Coord => Coord): None.type = None
  override def selection: None.type = None
  override def toJson:Option[JsonStroke] = None
  override def toJsonString:Option[String] = None
  override def toShortJsonString:Option[String] = None

}
