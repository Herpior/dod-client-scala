package dmodel

import com.google.gson.Gson
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
}

class JsonOk{
  var ok:Boolean = _
  def isOk = ok
}