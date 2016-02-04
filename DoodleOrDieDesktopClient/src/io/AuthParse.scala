package io

import collection.mutable.Map

/**
 * @author Herpior
 */
object AuthParse {
  def parseTwitter(in:Array[String])={
    val inputs = Map[String,String]()
    for (line <- in){
      val trimmed = line.trim()
      if (trimmed.startsWith("<input")){
        var name = ""
        var value = ""
        for (param <- trimmed.drop(7).split(' ')){
          if (param.trim().startsWith("name=\"")){
            name = param.trim().drop(6).takeWhile { _!='"' }
          } else if (param.startsWith("value=\"")){
            value = param.trim().drop(7).takeWhile { _!='"' }
          }
        }
        //println(name)
        //println(value)
        if ((name=="authenticity_token" || name=="redirect_after_login" || name =="oauth_token")
            && value.length()>0){
          inputs(name)=value
        }
      }
    }
    //println("AuthParse parseTwitter")
    //println(inputs.mkString("\n"))
    inputs
  }
  def parseTwitterCallback(in:Array[String]):String = {
    val text = "<meta"
    for (line <- in){
      if(line.trim().startsWith(text)){
        val cb = line.split(';').last
        if (cb.startsWith("url=")) return cb.dropWhile { _!='h' }.takeWhile { _!='"' }
      }
    }
    ""
  }
}