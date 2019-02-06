package dmodel.dpart

// the doodle format for doodles on doodleordie servers? I think..
class JsonDoodle {
  var version: Int = _
  var doodle_id: String = _
  var user_id: String = _
  var date: String = _
  var time: Int = _
  var count: Int = _
  var width: Int = _
  var height: Int = _
  var ext: String = _
  var url: String = _
  var strokes: Array[JsonLine] = Array()
  
  def getStrokes:Array[JsonLine] = if(strokes.isEmpty)http.HttpHandler.getDoodle(url).getStrokes else strokes
  
  //def print = println(this)
  override def toString: String ="version "+version+". doodle_id "+doodle_id+". user_id "+user_id+". date "+date+". time "+time+
      ". count "+count+". width "+width+". height "+height+". ext "+ext+". strookes "+strokes.take(10).mkString(", ")
}