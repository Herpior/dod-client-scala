package dmodel

/**
  * An object using gson to parse data from the servers.
  *

  * @author Qazhax
  */

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.StringReader
import java.io.BufferedReader

import dmodel.dpart.JsonDoodle

object JsonParse {
    val gson = new Gson

  def parseDoodle(jsfile:String): JsonDoodle ={
    //val gson = new Gson
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
  def parseSave(jsfile:String): JsonSave ={
    //val gson = new Gson
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
  def parseState(jsfile:String): JsonState ={
    //val gson = new Gson
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
    //val gson = new Gson
    //println(jsfile)
    //val red = new JsonParser
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    try{
      val targ = gson.fromJson(reader,classOf[JsonOk])
      targ
    }catch{
      case e:Throwable=>new JsonOk{ok=false}
    }
    //targ.print
    //println("end")
    //doodle.print
  }
  def parseSkips(jsfile:String): JsonSkips ={
    //val gson = new Gson
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonSkips])
    targ
  }
  def parseGroupList(jsfile:String): JsonGroups ={
    val gson = new Gson
    val reader = new BufferedReader(new StringReader(jsfile))
    //reader.setLenient(true)
    val targ = gson.fromJson(reader,classOf[JsonGroups])
    targ
  }
  def writeSave(layers:Array[Layer], time:Int): String ={
    //Don't use, too slow and adds useless extra values
    
    //val gson = new Gson
    //var before = System.nanoTime
    val save = new JsonSave
    val buf = collection.mutable.Buffer[JsonLayer]()
    layers.foreach{
      layer =>
        val jsonlayer = new JsonLayer
        jsonlayer.strokes = layer.getStrokes.flatMap(_.toJson)
        jsonlayer.visible = layer.isVisible
        buf += jsonlayer
    }
    save.layers = buf.toArray
    save.version = 425
    save.time = time
    save.doodle_id = http.HttpHandler.state.private_id
    //var after = System.nanoTime
    //print("other "+(after-before))
    
    //before = System.nanoTime
    //val res = 
    gson.toJson(save)//, new java.io.FileWriter("layersfile.json"))
    //after = System.nanoTime
    //print("tojson "+(after-before))
    //res
  }
}
