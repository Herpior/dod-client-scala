package http

import org.apache.http.impl.client.HttpClients
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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.cookie.ClientCookie//.DOMAIN_ATTR
//import org.apache.http.client.utils.URIBuilder
//import org.apache.http.client.params
import org.apache.http.client.config.{RequestConfig, CookieSpecs}
import java.io.File
import java.util.Calendar


object HttpHandler {

  //val classLoader = getClass().getClassLoader();
  //println("lol")
  val url = getClass.getResource("/jssecacerts")
  val keystore = new File(url.getFile())
  val sslcontext = SSLContexts.custom()
                .loadTrustMaterial(keystore, "changeit".toCharArray(),
                        new TrustSelfSignedStrategy())
                .build();
  val sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                Array( "TLSv1" ),
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
  val httpCookieStore = new BasicCookieStore();
  val globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build(); 
  val client = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(httpCookieStore)
                .build();//HttpClientBuilder.create().build();//new DefaultHttpClient .setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
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
    println(httpCookieStore.getCookies)
  }catch{case e:Throwable=>println("load cid failed")}
  
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
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent))
        }
        else new java.util.Scanner(response.getEntity.getContent)
    val str = Buffer[String]()
    println()
    println("HttpHandler getHttp")
    println(" post headers")
    println(post.getAllHeaders.mkString("\n"))
    println(" response headers")
    println(response.getAllHeaders.mkString("\n"))
    println("post: "+post.getClass)//remove
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
    
    println()
    println("HttpHandler getHttp")
    println(" get headers")
    println(get.getAllHeaders.mkString("\n"))
    println(" response headers")
    println(response.getAllHeaders.mkString("\n"))
    val in = 
        if (GZIP_CONTENT_TYPE.equals(response.getEntity.getContentEncoding())){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent))
        }
        else new java.util.Scanner(response.getEntity.getContent)
    val str = Buffer[String]()
    println("get: "+get.getClass)
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
  def submitDoodle(pp:Int,pt:Int,doodle:Array[BasicLine])={//
    val post = new DoodlePost(room,chain,doodle,pp,pt)//,cookie)
    //val out = new java.util.Scanner(post.getEntity.getContent)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
//    println(in.mkString("\n, "))
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
    //while(in.hasNext){
      //println(in.nextLine())
    //}
    //while(out.hasNext){
      //println(out.nextLine())
   // }
    JsonParse.parseOk(in.mkString("\n")).isOk
    //false
  }
  def logout {
    println(httpCookieStore.getCookies)
    var get = new DodGet("bye","")
     getHttp(get)
    room = "global"
    chain = ""
    auth = new dmodel.JsonSkips
    val get3 = new MainGet()
    val in =  getHttp(get3)
    println("HttpHandler logout")
    println("in:")
    //println(in.mkString("\n"))
    println(httpCookieStore.toString())
  }
  def login(password:Array[Char],username:String):Boolean={
    
    val post = new LoginPost(password,username)
    val in = postHttp(post)
    if(in.mkString("\n")!="""<p>Moved Temporarily. Redirecting to <a href="https://doodleordie.com/">https://doodleordie.com/</a></p>""")return false
      //println(in.mkString("\n"))
      //if not ok return false
    //println(_conn)
    val get = new MainGet()
    val in2 = getHttp(get)
    //println(in2.mkString("\n"))
    //println(_conn)
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
  def faceLogin(user:String,pw:Array[Char])={
    false//TODO make facebook login
  }
  def twitterLogin(user:String,pw:Array[Char])={
    println("HttpHandler twitterLogin")
    print("cid: ")
    println(cid)
    //println(cookie)
    val get = new DodGet("auth/twitter?returnTo=%2Fsignin/signin","signin")
    val in = getHttp(get)
    println("HttpHandler twitterLogin")
    print("cid: ")
    println(cid)
    //println(cookie)
    val inputs = io.AuthParse.parseTwitter(in)
    val post = new TwitterPost(user,pw,inputs)
    val in2 = postHttp(post)
    println("HttpHandler twitterLogin")
    print("cid: ")
    println(cid)
    println()
    println("HttpHandler twitterLogin")
    println("in2:")
    //println(in2.mkString("\n"))
    val callback = io.AuthParse.parseTwitterCallback(in2)
    if(callback.length()>0){
      println(callback)
      val get2 = new DodGet(callback.drop("http://doodleordie.com/".length),"")
      //text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8 //        <------
      val in3 = getHttp(get2)
    println("HttpHandler twitterLogin")
    println("in3:")
    //println(in3.mkString("\n"))
    val get3 = new MainGet()
      getHttp(get3)
    !cid.isEmpty
    } else false
    //println(in2.mkString("\n"))
    //false//TODO make twitter login work //TODO find out why does it work
  }
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
    println(cid)
    httpCookieStore.getCookies.find { cookie => cookie.getName=="cid" }.foreach{
      x=> println(x.getDomain)
      println(x.getExpiryDate)
      println(x.getPath)
      println(x.getPorts)
      println(x.getVersion)
      println(!cid.isEmpty)
    }
    
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
  def ping{
    var f :Future[Boolean] = Future{
      val post = new PingPost(chain)
      val in = postHttp(post)
      //println(in.mkString("\n, "))
      JsonParse.parseOk(in.mkString("\n")).isOk
    }
    f.onSuccess{
      case b => 
        if(!b) f = Future{
          val post = new PingPost(chain)
          val in = postHttp(post)
          //println(in.mkString("\n, "))
          JsonParse.parseOk(in.mkString("\n")).isOk
        }
    }
  }
}