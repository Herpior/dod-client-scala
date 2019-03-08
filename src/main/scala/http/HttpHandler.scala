package http

/**
  * An object handling all http connections, and maintaining the state between the server and client, e.g. login status.

  * @author Qazhax
  */

import java.io.FileNotFoundException

import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpGet

import collection.mutable.Buffer
import scala.collection.JavaConversions._
import org.apache.http.util.EntityUtils
import dmodel.{JsonGroups, JsonParse, JsonSkips, JsonState}
import java.util.zip.GZIPInputStream

import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.cookie.ClientCookie
import java.util.Calendar

import dmodel.dpart.{BasicLine, JsonDoodle}


object HttpHandler {

  private var client :CloseableHttpClient = _
  val httpCookieStore = new BasicCookieStore()
  val cacertFilename = "/dodcacerts"
  val cacertPass: Array[Char] = "RpcQNp1tTSifL6aGqAtt".toCharArray

  try{
    client = KeystoreLoader.setUpClient(cacertFilename, cacertPass, httpCookieStore)
    } catch {
      case e: IllegalArgumentException => //TODO:add notification that regular login is not possible without the key?
        println("dodcacerts not found in the jar: " + e)
      case e:Throwable =>
        throw e
  }
  

  def cid:Option[String] = {
    httpCookieStore.getCookies.find { cookie => cookie.getName=="cid" }.map(_.getValue)
  }
  
  try{
    addDodCookie("cid",io.Crypt.decipherFrom("login"))
  }
  catch{
    case e:FileNotFoundException=> // just means that user has not enabled automatic login
    case e:Throwable=>println("load cid failed: "+e)
  }

  private var chain = ""
  private var room = "global"
  private var auth = new dmodel.JsonSkips
  
  val GZIP_CONTENT_TYPE = "gzip"
  def getChain: String = chain
  def getGroup: String = room
  def getAuth: Boolean = auth.isSuper
  def loggedIn: Boolean = auth.isLoggedIn

  def saveCid() {
  cid.foreach { x => io.Crypt.encipherTo(x, "login") }
  }
    
  private def postHttp(post:HttpPost)={
    val response = client.execute(post)
    val in = 
        if (response.getEntity.getContentEncoding.getElements.exists(codec=>codec.getName.equalsIgnoreCase(GZIP_CONTENT_TYPE))){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent), "utf-8")
        }
        else new java.util.Scanner(response.getEntity.getContent, "utf-8")
    val str = Buffer[String]()
    while(in.hasNext){
      str += in.nextLine()
    }
    EntityUtils.consume(response.getEntity)
    str.toArray
  }

  def getHttp(get:HttpGet): Array[String] ={
    val response = client.execute(get)
    val enc = response.getEntity.getContentEncoding
    val isGzip = enc != null && enc.getElements.exists(codec=>codec.getName.equalsIgnoreCase(GZIP_CONTENT_TYPE))
    val in =
      if (isGzip){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent), "utf-8")
        }
        else new java.util.Scanner(response.getEntity.getContent, "utf-8")
    val str = Buffer[String]()
    while(in.hasNext){
      str += in.nextLine()
    }
    EntityUtils.consume(response.getEntity)
    str.toArray
  }

  private def addDodCookie(name:String,value:String){
    val cookie = new BasicClientCookie(name,value)
    cookie.setDomain("doodleordie.com")
    cookie.setPath("/")
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 300)
    val date = calendar.getTime
    cookie.setExpiryDate(date)
    cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true")
    httpCookieStore.addCookie(cookie)
  }

  def submitDoodle(doodleJson:String):Boolean={
    val post = new DoodlePost(chain,doodleJson)
    val in = postHttp(post)
    //println(doodleJson)
    //println(in.mkString("\n"))
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def submitDesc(desc:String): Boolean ={
    val post = new DescPost(chain,desc)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def logout() {
    val get = new DodGet("bye","")
    getHttp(get)
    room = "global"
    chain = ""
    auth = new dmodel.JsonSkips
    val get3 = new MainGet()
    val in =  getHttp(get3)
    dmodel.Magic.user = ""
  }
  def login(password:Array[Char],username:String):Boolean={

    try {
      val post = new LoginPost(password, username)
      val in = postHttp(post)
      if (in.mkString("\n") !="""<p>Moved Temporarily. Redirecting to <a href="https://doodleordie.com/">https://doodleordie.com/</a></p>""") return false

      val get = new MainGet()
      val in2 = getHttp(get)
      setUsername(in2)
    } catch {
      case e:NullPointerException =>
        println("the cert is not working so can't connect to the site")
    }
    cid.isDefined
  }
  def skip(){
    val post = new SkipPost(chain)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def getSkips: JsonSkips = {
    val get = new SkipsGet()
    val in = getHttp(get)
    val skips = JsonParse.parseSkips(in.mkString("\n"))
    this.auth = skips
    skips
  }
  def getGroupList: JsonGroups ={
    val get = new GroupsGet()
    val in = getHttp(get)
    val list = JsonParse.parseGroupList(in.mkString("\n"))
    if(list.activeGroup!=null&&list.activeGroup._id!=null) room = list.activeGroup._id
    list
  }
  def getDoodle(url:String): JsonDoodle ={
    val get = new DoodleGet(url)
    val in = getHttp(get)
    JsonParse.parseDoodle(in.mkString("\n").dropWhile(_!='{').dropRight(2))
  }
  def getDoodleAndResponse(url:String) ={
    val get = new DoodleGet(url)
    val in = getHttp(get)
    (in.mkString("\n"), JsonParse.parseDoodle(in.mkString("\n").dropWhile(_!='{').dropRight(2)))
  }
  def getProfileData(player:String,page:Int,per_page:Int=12,steps:String="doodles",sort:String="date") ={
    val get = new ProfileGet(player, steps, sort, page, per_page)
    val in = getHttp(get)
    JsonParse.parseProfile(in.mkString("\n"))
  }
  def changeGroup(group_id:String): Boolean ={
    val post = new ChangeGroupPost(group_id)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def cookieLogin(pw:Array[Char]):Boolean={
    addDodCookie("cid", pw.mkString)
    val get = new MainGet()
    val in2 = getHttp(get)
    setUsername(in2)
    
    cid.isDefined
  }
  def debugGet(ext:String,ref:String): Unit ={
    val get = new DefaultGet(ext,ref)
    val in = getHttp(get)
    println("HttpHandler debugGet")
    println("in:")
    println(in.mkString("\n"))
    println("cookies:")
  }
  def hasCid: Boolean = {
    cid.isDefined
  }
  def state: JsonState ={
    val get = new StateGet()
    val in = getHttp(get)
    val state = JsonParse.parseState(in.mkString("\n"))
    chain = state.private_id
    if(chain == null) chain = ""
    if(state.group_id!=null)room = state.group_id
    if(state.skipsInfo != null )auth = state.skipsInfo
    state
  }
  def resume: Boolean ={
    val post = new ResumePost()
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def ping: Boolean ={
    val post = new PingPost(chain)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def getAndSetUsername() {
    val get = new MainGet()
    val in = getHttp(get)
    setUsername(in)
  }
  def setUsername(mainget:Array[String]) {
    try {
      val prev = mainget.indexWhere { x => x.endsWith("""class="nav_you">""") }
      val ripped = mainget(prev+1).trim.dropRight(7).dropWhile{ x => x != '>' }.drop(1)
      dmodel.Magic.user = ripped
    } catch {
      case e:Throwable =>
    }
  }
}
