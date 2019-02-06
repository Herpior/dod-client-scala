package dmodel.dpart

// the doodle format for doodles on doodleordie servers? I think..
class JsonDoodle {
  val version: Int = _
  val doodle_id: String = _
  val user_id: String = _
  val date: String = _
  val time: Int = _
  val count: Int = _
  val width: Int = _
  val height: Int = _
  val ext: String = _
  val url: String = _
  val strokes: Array[JsonLine] = Array()
  
  def getStrokes:Array[JsonLine] = if(strokes.isEmpty)http.HttpHandler.getDoodle(url).getStrokes else strokes
  
  //def print = println(this)
  override def toString: String ="version "+version+". doodle_id "+doodle_id+". user_id "+user_id+". date "+date+". time "+time+
      ". count "+count+". width "+width+". height "+height+". ext "+ext+". strookes "+strokes.take(10).mkString(", ")
}