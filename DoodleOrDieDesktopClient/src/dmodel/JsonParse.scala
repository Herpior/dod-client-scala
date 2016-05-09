package dmodel

import com.google.gson.Gson
import com.google.gson.GsonBuilder;
import java.io.StringReader
import java.io.BufferedReader

object JsonParse {

  def parseDoodle(jsfile:String)={
    val gson = new Gson
    //println(jsfile)
    //val red = new JsonParser
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonDoodle])
    //targ.print
    //println("end")
    //doodle.print
    targ
  }
  def parseSave(jsfile:String)={
    val gson = new Gson
    //println(jsfile)
    //val red = new JsonParser
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonSave])
    //targ.print
    //println("end")
    //doodle.print
    targ
  }
  def parseState(jsfile:String)={
    val gson = new Gson
    //println(jsfile)
    //val red = new JsonParser
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonState])
    //targ.print
    //println("end")
    //doodle.print
    targ
  }
  def parseOk(jsfile:String):JsonOk={
    val gson = new Gson
    //println(jsfile)
    //val red = new JsonParser
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    try{
      val targ = gson.fromJson(reader,classOf[JsonOk])
      return targ
    }catch{
      case e:Throwable=>return new JsonOk{ok=false}
    }
    //targ.print
    //println("end")
    //doodle.print
  }
  def parseSkips(jsfile:String)={
    val gson = new Gson
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonSkips])
    targ
  }
  def parseGroupList(jsfile:String)={
    val gson = new Gson
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonGroups])
    targ
  }
  def testParse={
    val str = "{\"linetype\":\"bezier\",\"size\":5}"
    val str2 = "{\"linetype\":\"multi\",\"strokes\":[]}"
    val gson = new Gson
    val reader = new BufferedReader(new StringReader(str))
    val reader2 = new BufferedReader(new StringReader(str2))
    val targ = gson.fromJson(reader,classOf[JsonStroke])
    println(targ.toDoodlePart.getClass)
    val targ2 = gson.fromJson(reader2,classOf[JsonStroke])
    println(targ2.toDoodlePart.getClass)
  }
  def writeSave(layers:Array[Layer], time:Int)={
    val gson = new Gson
    val save = new JsonSave
    val buf = collection.mutable.Buffer[JsonLayer]()
    layers.foreach{
      layer =>
        val jsonlayer = new JsonLayer
        jsonlayer.strokes = layer.getStrokes(true).map(_.toJson)
        buf += jsonlayer
    }
    save.layers = buf.toArray
    save.version = 425
    save.time = time
    save.doodle_id = http.HttpHandler.state.private_id
    gson.toJson(save)//, new java.io.FileWriter("layersfile.json"))
  }
}

class JsonOk{
  var ok:Boolean = _
  def isOk = ok
}