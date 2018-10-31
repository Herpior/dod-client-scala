package http

import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpGet

import scala.concurrent.Future
import view._

import collection.mutable.Buffer
import scala.collection.JavaConversions._
import org.apache.http.util.EntityUtils
import dmodel.BasicLine
import dmodel.JsonParse

import scala.concurrent.ExecutionContext.Implicits.global
import java.util.zip.GZIPInputStream

import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.ssl.SSLContexts
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.cookie.ClientCookie

import scala.io.Source//.DOMAIN_ATTR
//import org.apache.http.client.utils.URIBuilder
//import org.apache.http.client.params
import org.apache.http.client.config.{RequestConfig, CookieSpecs}
import java.io.File
import java.util.Calendar


object HttpHandler {

  //val classLoader = getClass().getClassLoader();
  //println("lol")
  private var client :CloseableHttpClient = null//HttpClients.custom().build()
  val httpCookieStore = new BasicCookieStore()
  val cacertPath = "/dodcacerts"
  val cacertPass = "RpcQNp1tTSifL6aGqAtt".toCharArray()

  try{
    client = KeystoreLoader.setUpClient(cacertPath, cacertPass, httpCookieStore)
    } catch {
      case e: NullPointerException => //TODO:add notification that regular login is not possible without the key?
        println("dodcacerts not found")
      case e =>
        throw e
  }
  
  
  //val uriBuilder = new URIBuilder()
  //uriBuilder.setParameter(params.ClientPNames.COOKIE_POLICY,params.CookiePolicy.BROWSER_COMPATIBILITY);
  //private def cookie = Array(cid,_conn).filter(_.trim.nonEmpty).mkString(";")//if(cid.isEmpty())_conn else if(_conn.isEmpty)cid else Array(cid,_conn).mkString(";")//
  //def cook = cookie
  //private var cid = 
  def cid:Option[String] = {
    httpCookieStore.getCookies.find { cookie => cookie.getName=="cid" }.map(_.getValue)
    /*foreach { 
      cookie => 
        if(cookie.getName=="cid")
          return cookie.getValue
    } 
    ""*/
  }
  
  try{
    addDodCookie("cid",io.Crypt.decipherFrom("login"))
  }catch{case e:Throwable=>println("load cid failed - "+e.getLocalizedMessage)}
  
  //private var _conn = ""
  private var chain = ""
  //private var twid = ""
  //private var auth_token = ""
  private var room = "global"
  private var auth = new dmodel.JsonSkips
  
  val GZIP_CONTENT_TYPE = "gzip"
    def getChain = chain
    def getGroup = room
    def getAuth = auth.isSuper
    def loggedIn = auth.skipsPerDuration>0 //very weird way to figure out if logged in
  
    def saveCid {
    cid.foreach { x => io.Crypt.encipherTo(x, "login") }
  }
    
  private def postHttp(post:HttpPost)={
    //val out = new java.util.Scanner(post.getEntity.getContent)
    //while(out.hasNext){
    //  println(out.nextLine())
    //}
    val response = client.execute(post)
    //val cooky = response.getHeaders("Set-Cookie").map(_.getValue.takeWhile ( _ != ';' ))//
    //println("cooky: "+cooky.mkString("; "))
    ////cooky.find { x => x.takeWhile { c => c!='=' }.trim =="twid"}.foreach(s=>twid = s)
    ////cooky.find { x => x.takeWhile { c => c!='=' }.trim =="auth_token"}.foreach(s=>auth_token = s)
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="cid"}.foreach(s=>addDodCookie("cid",s))//cid = s)
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="__conn"}.foreach(s=>addDodCookie("_conn",s))//_conn = s)
    //cooky.foreach { x=>println(x.takeWhile { c => c!=':' }.trim) }
    val in = 
        if (GZIP_CONTENT_TYPE.equals(response.getEntity.getContentEncoding())){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent), "utf-8")
        }
        else new java.util.Scanner(response.getEntity.getContent, "utf-8")
    val str = Buffer[String]()
    /*println()
    println("HttpHandler getHttp")
    println(" post headers")
    println(post.getAllHeaders.mkString("\n"))
    println(" response headers")
    println(response.getAllHeaders.mkString("\n"))
    println("post: "+post.getClass)//remove*/
    while(in.hasNext){
      str+=(in.nextLine())
    }
    EntityUtils.consume(response.getEntity());
    str.toArray
  }
  def getHttp(get:HttpGet)={
    val response = client.execute(get)
    val cooky = response.getHeaders("Set-Cookie").map(_.getValue.takeWhile ( _ != ';' ))//
    //println("cooky: "+cooky.mkString("; "))
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="cid"}.foreach(s=>addDodCookie("cid",s))//cid = s)
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="__conn"}.foreach(s=>addDodCookie("_conn",s))//_conn = s)
    
    /*println()
    println("HttpHandler getHttp")
    println(" get headers")
    println(get.getAllHeaders.mkString("\n"))
    println(" response headers")
    println(response.getAllHeaders.mkString("\n"))*/
    val in = 
        if (GZIP_CONTENT_TYPE.equals(response.getEntity.getContentEncoding())){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent), "utf-8")
        }
        else new java.util.Scanner(response.getEntity.getContent, "utf-8")
    val str = Buffer[String]()
    //println("get: "+get.getClass)
    while(in.hasNext){
      str+=(in.nextLine())
    }
    EntityUtils.consume(response.getEntity());
    str.toArray
  }
  private def addDodCookie(name:String,value:String){
    val cookie = new BasicClientCookie(name,value)
    cookie.setDomain("doodleordie.com");
    cookie.setPath("/")
    val calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 300);
    val date = calendar.getTime();
    cookie.setExpiryDate(date);
    cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
    httpCookieStore.addCookie(cookie)
  }
  /*
   * value pp: PaintPercentage
   * value pt: PaintTime
   * value doodle: array of strokes
   */
  def submitDoodle(pp:Int,pt:Int,doodle:Array[BasicLine]):Boolean={//
    val post = new DoodlePost(room,chain,doodle,pp,pt)//,cookie)
    //val out = new java.util.Scanner(post.getEntity.getContent)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
    //println(doodle.mkString("\n, "))
    //while(in.hasNext){
      //println(in.nextLine())
    //}
    //while(out.hasNext){
    //  println(out.nextLine())
    //}
    //true
    //false
  }
  def submitDesc(desc:String)={
    val post = new DescPost(chain,desc)
    val in = postHttp(post)
    //println(in.mkString("\n"))
    JsonParse.parseOk(in.mkString("\n")).isOk
    //false
  }
  def logout {
    //println(httpCookieStore.getCookies)
    var get = new DodGet("bye","")
     getHttp(get)
    room = "global"
    chain = ""
    auth = new dmodel.JsonSkips
    val get3 = new MainGet()
    val in =  getHttp(get3)
    dmodel.Magic.user = ""
    //println("HttpHandler logout")
    println("in:")
    println(in.mkString("\n"))
    //println(httpCookieStore.toString())
  }
  def login(password:Array[Char],username:String):Boolean={

    try {
      val post = new LoginPost(password, username)
      val in = postHttp(post)
      if (in.mkString("\n") !="""<p>Moved Temporarily. Redirecting to <a href="https://doodleordie.com/">https://doodleordie.com/</a></p>""") return false
      //println("in\n" + in.mkString("\n"))
      //if not ok return false
      //println(_conn)
      val get = new MainGet()
      val in2 = getHttp(get)
      setUsername(in2)
      //println("in2\n" + in2.mkString("\n"))
      //println(_conn)
    } catch {
      case e:NullPointerException => //the cert is not working so can't connect to the site, TODO:add dialogue to user about this
    }
    !cid.isEmpty
  }
  def skip{
    val post = new SkipPost(chain)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
    //???
  }
  def getSkips={
    val get = new SkipsGet()
    val in = getHttp(get)
    val skips = JsonParse.parseSkips(in.mkString("\n"))
    this.auth = skips
    skips
  }
  def getGroupList={
    val get = new GroupsGet()
    val in = getHttp(get)
    val list = JsonParse.parseGroupList(in.mkString("\n"))
    if(list.activeGroup!=null&&list.activeGroup._id!=null) room = list.activeGroup._id
    list
  }
  def getDoodle(url:String)={
    val get = new DoodleGet(url)
    val in = getHttp(get)
    JsonParse.parseDoodle(in.mkString("\n").dropWhile(_!='{').dropRight(2))
  }
  def changeGroup(group_id:String)={
    val post = new ChangeGroupPost(group_id)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def cookieLogin(pw:Array[Char]):Boolean={
    addDodCookie("cid", pw.mkString)
    val get = new MainGet()
    val in2 = getHttp(get)
    setUsername(in2)
    
    !cid.isEmpty
  }
  /*
  def faceLogin(user:String,pw:Array[Char]):Boolean={
    import org.openqa.selenium.WebDriver
    import org.openqa.selenium.htmlunit.HtmlUnitDriver
    import org.openqa.selenium.remote.DesiredCapabilities
    val caps = new DesiredCapabilities
    caps.setJavascriptEnabled(true)
    val driver = new HtmlUnitDriver()
    driver.getBrowserVersion().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    driver.get("https://doodleordie.com/auth/facebook?returnTo=%2Fsignin")
    //driver.get("https://www.facebook.com/dialog/oauth?response_type=code&redirect_uri=http%3A%2F%2Fdoodleordie.com%2Fauth%2Ffacebook%2Fcallback&client_id=281697128537109")
    //println("facelogin")
    //println(driver.getTitle)
    //println(driver.getCurrentUrl)
    //println(driver.getPageSource)
    try{
    driver.findElementById("email").sendKeys(user)
    driver.findElementById("pass").sendKeys(pw)
    driver.findElementByName("login").click()
    }catch{
      case e:Throwable=>
        driver.close()
        return false
    }
    //println("facelogin")
    //println(driver.getTitle)
    //println(driver.getCurrentUrl)
    //println(driver.getPageSource)
    try{
      addDodCookie("cid",driver.manage().getCookieNamed("cid").getValue)
    }
    catch{
      case e:NullPointerException => 
    }
    driver.close()
    //use selenium
    !cid.isEmpty//false//TODO make face login work
  }
  */
  /*
  def twitterLogin(user:String,pw:Array[Char]):Boolean={
    import org.openqa.selenium.WebDriver
    import org.openqa.selenium.htmlunit.HtmlUnitDriver
    import org.openqa.selenium.remote.DesiredCapabilities
    val caps = new DesiredCapabilities
    caps.setJavascriptEnabled(true)
    val driver = new HtmlUnitDriver()
    driver.getBrowserVersion().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
    driver.get("https://doodleordie.com/auth/twitter?returnTo=%2Fsignin")
    //driver.get("https://www.facebook.com/dialog/oauth?response_type=code&redirect_uri=http%3A%2F%2Fdoodleordie.com%2Fauth%2Ffacebook%2Fcallback&client_id=281697128537109")
    //println("twitterlogin")
    //println(driver.getTitle)
    //println(driver.getCurrentUrl)
    //println(driver.getPageSource)
    try{
    driver.findElementById("username_or_email").sendKeys(user)
    driver.findElementById("password").sendKeys(pw)
    driver.findElementById("allow").click()
    }catch{
      case e:Throwable=>
        driver.close()
        return false
    }
    //println("twitterlogin")
    //println(driver.getTitle)
    //println(driver.getCurrentUrl)//improve error message with the url?
    //println(driver.getPageSource)
    try{
      addDodCookie("cid",driver.manage().getCookieNamed("cid").getValue)
    }
    catch{
      case e:NullPointerException => 
    }
    driver.close()
    //use selenium
    getAndSetUsername()
    !cid.isEmpty
    /*
    val prepost = new DefaultPost("https://twitter.com/logout","https://twitter.com/")
    val prein = postHttp(prepost)
    
    val get = new DodGet("auth/twitter?returnTo=%2Fsignin/signin","signin")
    val in = getHttp(get)
    
    val inputs = io.AuthParse.parseTwitter(in)
    val post = new TwitterPost(user,pw,inputs)
    val in2 = postHttp(post)
    
    //println("HttpHandler twitterLogin")
    //println("in2:")
    //println(in2.mkString("\n"))
    
    val callback = io.AuthParse.parseTwitterCallback(in2)
    if(callback.length()>0){
      //println(callback)
      
      val get2 = new DodGet(callback.drop("http://doodleordie.com/".length),"")
      * 
      */
      //text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,* / *;q=0.8 //        <------remove spaces if this monstrosity is ever revived
      /*val in3 = getHttp(get2)
      
      //println("HttpHandler twitterLogin")
      //println("in3:")
      //println(in3.mkString("\n"))
      
      val get3 = new MainGet()
      getHttp(get3)
      !cid.isEmpty
    } else false
    //println(in2.mkString("\n"))
    //false//TODO make twitter login work //TODO find out why does it work
     * 
     */
  }
*/
  def debugGet(ext:String,ref:String)={
    val get = new DefaultGet(ext,ref)
    val in = getHttp(get)
    println("HttpHandler debugGet")
    println("in:")
    println(in.mkString("\n"))
    println("cookies:")
    //println(cookie)
    //
  }
  def hasCid = {
    //println(cid)
    /*httpCookieStore.getCookies.find { cookie => cookie.getName=="cid" }.foreach{
      x=> println(x.getDomain)
      println(x.getExpiryDate)
      println(x.getPath)
      println(x.getPorts)
      println(x.getVersion)
      println(!cid.isEmpty)
    }*/
    !cid.isEmpty//this.cid!=null&&this.cid.length()>0
  }
  def state={
    val get = new StateGet()
    val in = getHttp(get)
    val state = JsonParse.parseState(in.mkString("\n"))
    chain = state.private_id
    if(chain == null) chain = ""
    if(state.group_id!=null)room = state.group_id
    if(state.skipsInfo != null )auth = state.skipsInfo
    /*if(room == null) room = state.staleChain_id
    if(room == null) room = "global"*/
    state
  }
  def resume={
    val post = new ResumePost()
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def ping={
    val post = new PingPost(chain)
    val in = postHttp(post)
    //println(in.mkString("\n, "))
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
