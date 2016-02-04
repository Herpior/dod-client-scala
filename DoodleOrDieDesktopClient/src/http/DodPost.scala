package http

import collection.mutable.Map
import java.util.ArrayList
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair


import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import dmodel.BasicLine

class DefaultPost(url:String,ref:String) extends HttpPost(url){
    //if(cookie.length>0)this.addHeader("Cookie",cookie)
    if(ref.length()>0)this.addHeader("Referer",ref)
    this.addHeader("Accept-Encoding","gzip")//,deflate")
    this.addHeader("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6")
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
  
}
class DodPost(s:String,ext:String,ref:String) extends DefaultPost("http"+s+"://doodleordie.com/"+ext,"http"+s+"://doodleordie.com/"+ref){
    this.addHeader("Origin","http://doodleordie.com")
    this.addHeader("x-js-ver","7f183e22")
    this.addHeader("X-Requested-With","XMLHttpRequest")
    this.addHeader("Connection","keep-alive")
  //"""-H "Origin: http://doodleordie.com" 
  //   -H "Accept-Encoding: gzip,deflate"      
  //   -H "x-js-ver: 7f183e22" 
  //   -H "Accept-Language: fi-FI,fi;q=0.8,en-US;q=0.6,en;q=0.4" 
  //   -H "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36" 
  //   -H "Content-Type: application/json" 
  //   -H "Accept: */*" 
  //   -H "Referer: http://doodleordie.com/play" 
  //   -H "X-Requested-With: XMLHttpRequest" 
  //   -H "Connection: keep-alive""""
}
class TwitterPost(username:String,password:Array[Char],input:Map[String,String]) extends DefaultPost("https://api.twitter.com/oauth/authorize","https://api.twitter.com/oauth/authorize?oauth_token="+input("oauth_token")){
  val nameValuePairs = new ArrayList[NameValuePair](1)
  nameValuePairs.add(new BasicNameValuePair("authenticity_token", input("authenticity_token")))
  nameValuePairs.add(new BasicNameValuePair("oauth_token", input("oauth_token")))
  nameValuePairs.add(new BasicNameValuePair("redirect_after_login", input("redirect_after_login")))
  nameValuePairs.add(new BasicNameValuePair("session[username_or_email]", username))
  nameValuePairs.add(new BasicNameValuePair("session[password]", password.mkString))
  this.setEntity(new UrlEncodedFormEntity(nameValuePairs))
}
class PlayPost(ext:String) extends DodPost("",ext,"play"){
  this.addHeader("Accept","*/*")
  this.addHeader("Content-Type","application/json")
}


class DoodlePost(room:String,chain:String,doodle:Array[BasicLine],pp:Int,pt:Int) extends PlayPost("api/game/submit/drawing/"+chain){
    
    
    val stookes = "["+doodle.mkString(",")+"]"
    var j = stookes.length
    var curr = 0
    def h(a:Int) = {
      val b = Math.ceil(j * (a / 100.0)).toInt
      //println(j+" - "+b)
      j -= b
      val res = "\""+stookes.substring(curr,curr+b).replaceAll("\"", "\\\\\"")+"\""
      curr += b
      res
    }
    //val pt = (math.random*100000 + stookes.length()*5).toInt
    def chpt(a:Int) = ",\""+chain+pt+""+a+"\":"
    val jsonpost = "{\"chain_id\":\""+chain+"\",\"group_id\":\""+room+"\",\"sc\":"+doodle.length+",\"pt\":"+pt+
    ",\"pp\":"+pp+",\"ext\":\"true\""+chpt(0)+j+chpt(3)+h(30)+chpt(2)+h(20)+chpt(1)+h(10)+chpt(5)+h(50)+chpt(4)+h(100)+"}"
    this.setEntity(new StringEntity(jsonpost))
}

class DescPost(chain:String,description:String)  extends PlayPost("api/game/submit/phrase/"+chain){
    
    val desc = description.take(140).replaceAll("<","&lt;").replaceAll(">","&gt;")
    
    this.setEntity(new StringEntity("{\"description\":\""+desc+"\"}"))
    
}

class LoginPost(password:Array[Char],username:String) extends DodPost("s","signin","signin"){
  this.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
  this.addHeader("Content-Type","application/x-www-form-urlencoded")
  //val send = "password="+password+"&username="+username
    val nameValuePairs = new ArrayList[NameValuePair](1)
    nameValuePairs.add(new BasicNameValuePair("username", username))
    nameValuePairs.add(new BasicNameValuePair("password", password.mkString))
    this.setEntity/*(new StringEntity(send))*/(new UrlEncodedFormEntity(nameValuePairs))
}
class ResumePost() extends PlayPost("api/game/resume"){
}

class SkipPost(chain:String) extends PlayPost("api/game/submit/skip/"+chain){
  //???
}
class ChangeGroupPost(group:String) extends PlayPost("api/group/changeActiveGroup"){
    this.setEntity(new StringEntity("{\"group_id\":\""+group+"\"}"))
  //curl "http://doodleordie.com/api/group/changeActiveGroup" -H "Cookie: rev_session_id=ed120225-2497-483b-a26e-f4302562feca; rev_current_session_id=2f6ccc45-5a3c-403a-9af0-7f128d58117f; rev_lr=1438848599342; rev_cs=1438848600883; __uvt=; cid=J2E1erWl7F; uvts=3OjlYFisx1eZ2Zp6; __conn=dygnnZoKfavfGj9VYSQMi27T.sqpwjWYdmnUDv"%"2FllXonm6U5U7xHbo1So8gss7vRIniI; __utmd=1; __utma=149446691.1267602900.1438848598.1438848598.1438848680.2; __utmb=149446691.1.9.1438851985369; __utmc=149446691; __utmz=149446691.1438848598.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)" -H "Origin: http://doodleordie.com" -H "Accept-Encoding: gzip, deflate" -H "x-js-ver: 7f183e22" -H "Accept-Language: fi-FI,fi;q=0.8,en-US;q=0.6,en;q=0.4" -H "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36" -H "Content-Type: application/json" -H "Accept: */*" -H "Referer: http://doodleordie.com/play" -H "X-Requested-With: XMLHttpRequest" -H "Connection: keep-alive" --data-binary "{""group_id"":""gJzy1S_3_""}" --compressed
}
class LikePost(doodle:String) extends DodPost("","api/steps/"+doodle+"/like",""){
  this.addHeader("Accept","*/*")
  this.addHeader("Content-Type","application/json")
  
}
class PingPost(chain:String) extends PlayPost("api/game/ping/doodle/"+chain){
  }