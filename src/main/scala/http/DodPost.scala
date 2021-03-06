package http

/**
  * A Superclass for post requests
  * subclasses often contain the static urls so the urls don't need to be defined elsewhere
  *

  * @author Qazhax
  */

import collection.mutable.Map
import java.util.ArrayList

import dmodel.dpart.BasicLine
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

class DefaultPost(url:String,ref:String) extends HttpPost(url){
    //if(cookie.length>0)this.addHeader("Cookie",cookie)
    if(ref.length()>0)this.addHeader("Referer",ref)
    this.addHeader("Accept-Encoding","gzip")//,deflate")
    this.addHeader("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6")
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
  
}

/** A class for post requests
  *
  * @param s either empty string or string containing "s". used for accessing https://doodleordie.com, ie. when logging in.
  * @param ext the target path on doodleordie.com domain, e.g. "play" if target is "http://doodleordie.com/play"
  * @param ref the ref path in on doodleordie.com domain, e.g. "play"
  */
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
}/*
* facebook is shitty and too secure to login like this
class FacePost(username:String,password:Array[Char],input:Map[String,String])extends DefaultPost("https://www.facebook.com/login.php?login_attempt=1&next=https%3A%2F%2Fwww.facebook.com%2Fv2.0%2Fdialog%2Foauth%3Fredirect_uri%3Dhttp%253A%252F%252Fdoodleordie.com%252Fauth%252Ffacebook%252Fcallback%26response_type%3Dcode%26client_id%3D281697128537109%26ret%3Dlogin&lwv=100",""){
  val nameValuePairs = new ArrayList[NameValuePair](1)
  nameValuePairs.add(new BasicNameValuePair("lsd", input("lsd")))
  nameValuePairs.add(new BasicNameValuePair("api_key", "281697128537109"))
  nameValuePairs.add(new BasicNameValuePair("redirect_after_login", input("redirect_after_login")))
  nameValuePairs.add(new BasicNameValuePair("email", username))
  nameValuePairs.add(new BasicNameValuePair("pass", password.mkString))
  this.setEntity(new UrlEncodedFormEntity(nameValuePairs))
}*/
/*
class TwitterPost(username:String,password:Array[Char],input:Map[String,String]) extends DefaultPost("https://api.twitter.com/oauth/authorize","https://api.twitter.com/oauth/authorize?oauth_token="+input("oauth_token")){
  val nameValuePairs = new ArrayList[NameValuePair](1)
  nameValuePairs.add(new BasicNameValuePair("authenticity_token", input("authenticity_token")))
  nameValuePairs.add(new BasicNameValuePair("oauth_token", input("oauth_token")))
  nameValuePairs.add(new BasicNameValuePair("redirect_after_login", input("redirect_after_login")))
  nameValuePairs.add(new BasicNameValuePair("session[username_or_email]", username))
  nameValuePairs.add(new BasicNameValuePair("session[password]", password.mkString))
  this.setEntity(new UrlEncodedFormEntity(nameValuePairs))
}*/
class PlayPost(ext:String) extends DodPost("",ext,"play"){
  this.addHeader("Accept","*/*")
  this.addHeader("Content-Type","application/json")
}


class DoodlePost(chain:String,dodPostJson:String) extends PlayPost("api/game/submit/drawing/"+chain){

    this.setEntity(new StringEntity(dodPostJson))
}

class DescPost(chain:String,description:String)  extends PlayPost("api/game/submit/phrase/"+chain){
    
    val desc: String = description.replace("\\","\\\\").replace("\"","\\\"")//.take(140)
    //println("{\"description\":\""+desc+"\"}")
    this.setEntity(new StringEntity("{\"description\":\""+desc+"\"}","UTF-8"))
    
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