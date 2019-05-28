package io

import java.io.File

import dmodel.Colors
import dmodel.dpart.BasicLine
import dmodel.json.{JsonLayer, JsonParse, JsonSave, JsonStroke}

object LocalStorage {

  //val source = scala.io.Source.fromFile("E:/trash/DOD/0-519.txt")
  //val values = try source.mkString finally source.close()
  val values: Array[Char] = Array.range(0, 1040).map(a=>(a+1e3).toChar)
  //(z[a] = String.fromCharCode(2 * a + 1e3), z[a])
  //var res = values//""
  //for(i<-values.indices){
  //  if(!res.exists(_==values(i))){
  //    res += values(i)
  //  }
  //}
  //println(values.mkString)
  val vmap: Map[Char, Int] = values.zipWithIndex.toMap.withDefault(x=>0)
  
  def transform(strokes:Array[BasicLine]): String ={
    //println(strokes.mkString(", "))
    /*"\\r"+*/ strokes.map { line =>
      val col = line.color
      val color = Colors.toHexString(col).drop(1)/*if(col(1)==col(2)&&col(3)==col(4)&&col(5)==col(6))col(1)+""+col(3)+""+col(5) else*/ /*col.drop(1)*/
      //x.checkPath
      //println(x.path.mkString(", "))
    line.getCoords.map(c=>cipher(c.x)+""+cipher(c.y)).mkString+"\\n"+values((line.size*2).toInt)+"\\n"+color
    }.mkString("\\t")// + "\\r"+strokes.length*17
  }
  def cipher(d:Double): Char ={
    values(math.min(math.max((d*2).toInt,0),1039))
  }
  def printToFile(f: java.io.File)(op: java.io.BufferedWriter => Unit) {
     try{f.getParentFile.mkdirs()}catch{case e:Throwable=>}
     val out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
    new java.io.FileOutputStream(f), "UTF-8"))
  try {
    op(out)
  } finally {
    out.close()
  }
}
  def printFile(strokes:Array[BasicLine],chain:String,pt:Int,path:String){
    printToFile(new File(path))
  {p=>
    val st = chain+"""\r"""+transform(strokes)+"""\r"""+pt
    p.write(st)}
  }
  def saveTo(json:String,chain:String){
    saveToPath(json,"saves/save."+chain+".txt" )
  }
  def saveToPath(json:String,path:String){
    printToFile(new File(path))
    {p=>
      val st = json
      p.write(st)}
  }
  def decryptFrom(path:String): JsonSave ={
    val encrypted = loadFrom(path)
    decrypt(encrypted)
  }
  def loadConfig(path:String): Map[String, String] ={
    val source = scala.io.Source.fromFile(path)("UTF-8")
    val derped = source.getLines().flatMap{
      str=>
        val arr = str.split('=').map(_.trim())
        if(arr.length == 2) Map[String, String](arr(0)->arr(1))
        else Map[String, String]()
    }
    derped.toMap
  }
  def loadSave(chain:String): JsonSave ={
    val loaded = loadFrom("saves/save."+chain+".txt")
    JsonParse.parseSave(loaded)
  }
  def loadFrom(path:String): String ={
    val ensource = scala.io.Source.fromFile(path)("UTF-8")
    //println(path)
    //println(System.getProperty("file.encoding"))
    //println(ensource.isEmpty)
    val lines = try ensource.mkString finally ensource.close()
    //ensource.close
    lines
    
  }
  def readArray(path:String): Array[String] ={
    val source = scala.io.Source.fromFile(path)("UTF-8")
    val res = source.mkString.split(",").map(_.trim)//(",(?=([^\\(]*\\([^\\)]*\\))*[^\(]*$)", -1)//split(",")
    source.close()
    res
  }
  def storeArray(arr:Array[String],path:String){
    //println(arr.mkString(","))
    printToFile(new File(path))
  {p=>
    val st = arr.mkString(",")
    p.write(st)}
  }/*
  def storePw(cid:String){
    Crypt.encipherTo(cid, "/login")
  }*/
  def decrypt(str:String):JsonSave={
    str.headOption.foreach{ c=>
      if (c=='{'){
        val save = JsonParse.parseSave(str)
        if (save.getLayers.isEmpty && save.version == 2){ //a dod-generated save that has already been cleaned
          val doodle = JsonParse.parseDoodle(str)
          return doodle.toJsonSave
        }
        return save
      }
      else if (c=='/'){
        val doodle = JsonParse.parseDoodle(str.dropWhile(_!='{').dropRight(2))
        return doodle.toJsonSave
      }
    }
    val parts = str.replaceAll("\"", "").split("\\\\r")
    //println("-1goTDRQ-f".toLowerCase.equals(parts.headOption.get.trim))
    //println(chain.length+" - "+parts.head.head)
    //println(chain +" = "+parts.headOption+parts.headOption.map(_.toString().equals(chain)/*http.HttpHandler.getChain*/))
    //println(parts.headOption)
   // if(parts.headOption.exists(_.equals(chain)||parts.headOption.exists(_.length<=1)/*http.HttpHandler.getChain*/)){
      val strokes = if(parts.length<2)parts(0) else parts(1)
      val starr = strokes/*.replaceAll("\\\\", "")*//*.replaceAll("nfff","nffffff")*/.split("\\\\t")
      val doodle = starr.map { x =>
       val pieces = x.split("\\\\n")
       //println(pieces.mkString(", "))
       val res = new JsonStroke
       res.path = pieces(0).toArray.map { x => vmap(x)/2.0 }
       if(res.path.length%2==1)res.path = res.path.drop(1)
       res.size = if(pieces.length>1)vmap(pieces(1)(0))/2 else 1
       res.color = if(pieces.length>2 && pieces(2).take(3)=="gba") "r"+pieces(2) else if(pieces.length>2) "#"+pieces(2)else "#000"
       //res.toBasicLine
       res
      }
      var pt = 0
      try 
        if(parts.length==3) 
          pt = Integer.parseInt(
              parts(2).trim()
              )
      catch{case e:Throwable=>e.printStackTrace()}
      val layer = new JsonLayer
      layer.strokes = doodle
      val save = new JsonSave
      save.layers = Array(layer)
      if(parts.nonEmpty) save.doodle_id = parts.headOption.get
      save.time = pt
      save
    /*} else {
      (Array[JsonLine](),0)
    }*/
  }
  
  
}