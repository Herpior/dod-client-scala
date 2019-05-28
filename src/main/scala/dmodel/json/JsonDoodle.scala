package dmodel.json

import dmodel.dpart.JsonLine

/**
  * Json class for gson to load doodles from the server.
  * the doodle format for doodles on doodleordie servers.
  *
  * @author Qazhax
  */
//TODO: move jsondoodle to json
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

  def toJsonSave = {
    val save = new JsonSave
    save.version = this.version
    save.doodle_id = this.doodle_id
    save.user_id = this.user_id
    save.time = this.time
    save.date = this.date
    val layer = new JsonLayer
    layer.strokes = this.getStrokes.flatMap(_.toJson)
    save.layers = Array(layer)
    save
  }
  
  //def print = println(this)
  override def toString: String ="version "+version+". doodle_id "+doodle_id+". user_id "+user_id+". date "+date+". time "+time+
      ". count "+count+". width "+width+". height "+height+". ext "+ext+". strookes "+strokes.take(10).mkString(", ")
}