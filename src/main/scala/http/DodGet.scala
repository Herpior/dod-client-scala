package http

import org.apache.http.client.methods.HttpGet

class DefaultGet(url:String,ref:String) extends HttpGet(url){
    this.addHeader("Accept-Encoding","gzip") //,deflate")//,sdch?
    this.addHeader("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6")
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    this.addHeader("Referer",ref)
    //this.addHeader("Cookie")
    this.addHeader("Accept","*/*")
}
class DodGet(ext:String,ref:String)  extends DefaultGet("http://doodleordie.com/"+ext,"http://doodleordie.com/"+ref){
    //this.addHeader("Origin","http://doodleordie.com")
    this.addHeader("x-js-ver","7f183e22")
    this.addHeader("Content-Type","application/json")
    this.addHeader("X-Requested-With","XMLHttpRequest")
    this.addHeader("Connection","keep-alive")
}

class MainGet() extends DodGet("",""){
}
class PlayGet() extends DodGet("play",""){
}
class DoodleGet(url:String) extends HttpGet(url){
    this.addHeader("Accept-Encoding","gzip,deflate")//,sdch?
    this.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    this.addHeader("Content-Type","application/json")
    this.addHeader("Accept","*/*")
    this.addHeader("X-Requested-With","XMLHttpRequest")
    this.addHeader("Connection","keep-alive")
    
    //this.addHeader("Cookie")
    
}
/*!brief
 * value player: player username in url form
 * value steps: type of steps, either "doodles" or "descriptions"(?)
 * value sort: sort type, either "date" or likes"(?)
 * value page: profile page number
 * value cookie: cookie with cid and __conn I guess
 */
class ProfileGet(player:String,steps:String,sort:String,page:Int) extends DodGet("api/players/"+player+"/steps/"+steps+"?sort="+sort+"&page="+page+"&format=html","profile/"+player){
}
class ChainGet() extends DodGet("",""){
}
class StateGet() extends DodGet("api/game/state.html","play"){
}
class GroupsGet() extends DodGet("api/groups?format=html","play"){
}
class SkipsGet() extends DodGet("api/game/skipsAvailable","play"){
}
// value group: id of group, empty if global.
class ExploreGet(group:String) extends DodGet("api/explore/get?format=html&group_id="+group+"&count=&skip=0&category=chains&granularity=day","r/"+group){
}
/*class PringProost(chain:String) extends DodPost("","ping/doodle/"+chain,""){
  }*/