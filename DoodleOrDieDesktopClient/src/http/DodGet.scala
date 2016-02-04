package http

import org.apache.http.client.methods.HttpGet

class DefaultGet(url:String,ref:String,cookie:String) extends HttpGet(url){
    this.addHeader("Accept-Encoding","gzip") //,deflate")//,sdch?
    this.addHeader("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6")
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    this.addHeader("Referer",ref)
    this.addHeader("Cookie",cookie)
    this.addHeader("Accept","*/*")
}
class DodGet(ext:String,ref:String,cookie:String)  extends DefaultGet("http://doodleordie.com/"+ext,"http://doodleordie.com/"+ref,cookie){
    //this.addHeader("Origin","http://doodleordie.com")
    this.addHeader("x-js-ver","7f183e22")
    this.addHeader("Content-Type","application/json")
    this.addHeader("X-Requested-With","XMLHttpRequest")
    this.addHeader("Connection","keep-alive")
}

class MainGet(cookie:String) extends DodGet("","",cookie){
}
class PlayGet(cookie:String) extends DodGet("play","",cookie){
}
class DoodleGet(url:String,cookie:String) extends HttpGet(url){
    this.addHeader("Accept-Encoding","gzip,deflate")//,sdch?
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    this.addHeader("Content-Type","application/json")
    this.addHeader("Accept","*/*")
    this.addHeader("X-Requested-With","XMLHttpRequest")
    this.addHeader("Connection","keep-alive")
    
    this.addHeader("Cookie",cookie)
    
}
/*!brief
 * value player: player username in url form
 * value steps: type of steps, either "doodles" or "descriptions"(?)
 * value sort: sort type, either "date" or likes"(?)
 * value page: profile page number
 * value cookie: cookie with cid and __conn I guess
 */
class ProfileGet(player:String,steps:String,sort:String,page:Int,cookie:String) extends DodGet("api/players/"+player+"/steps/"+steps+"?sort="+sort+"&page="+page+"&format=html","profile/"+player,cookie){
}
class ChainGet(cookie:String) extends DodGet("","",cookie){
}
class StateGet(cookie:String) extends DodGet("api/game/state.html","play",cookie){
}
class GroupsGet(cookie:String) extends DodGet("api/groups?format=html","play",cookie){
}
class SkipsGet(cookie:String) extends DodGet("api/game/skipsAvailable","play",cookie){
}
// value group: id of group, empty if global.
class ExploreGet(group:String,cookie:String) extends DodGet("api/explore/get?format=html&group_id="+group+"&count=&skip=0&category=chains&granularity=day","r/"+group,cookie){
}
/*class PringProost(chain:String,cookie:String) extends DodPost("","ping/doodle/"+chain,"",cookie){
  }*/