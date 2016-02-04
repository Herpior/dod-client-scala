package http

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpGet
import scala.concurrent.Future
import view._
import collection.mutable.Buffer
import org.apache.http.util.EntityUtils
import dmodel.BasicLine
import dmodel.JsonParse
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.zip.GZIPInputStream
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import java.io.File

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
  val client = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();//HttpClientBuilder.create().build();//new DefaultHttpClient
  private def cookie = if(cid.isEmpty())_conn else if(_conn.isEmpty)cid else Array(cid,_conn).mkString(";")//
  def cook = cookie
  private var cid = try{
    io.Crypt.decipherFrom("login")
  }catch{case e:Throwable=>""}
  private var _conn = ""
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
    io.Crypt.encipherTo(cid, "login")
  }
    
  private def postHttp(post:HttpPost)={
    //val out = new java.util.Scanner(post.getEntity.getContent)
    //while(out.hasNext){
    //  println(out.nextLine())
    //}
    val response = client.execute(post)
    val cooky = response.getHeaders("Set-Cookie").map(_.getValue.takeWhile ( _ != ';' ))//
    //println("cooky: "+cooky.mkString("; "))
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="twid"}.foreach(s=>twid = s)
    //cooky.find { x => x.takeWhile { c => c!='=' }.trim =="auth_token"}.foreach(s=>auth_token = s)
    cooky.find { x => x.takeWhile { c => c!='=' }.trim =="cid"}.foreach(s=>cid = s)
    cooky.find { x => x.takeWhile { c => c!='=' }.trim =="__conn"}.foreach(s=>_conn = s)
    //cooky.foreach { x=>println(x.takeWhile { c => c!=':' }.trim) }
    val in = 
        if (GZIP_CONTENT_TYPE.equals(response.getEntity.getContentEncoding())){
          new java.util.Scanner(new GZIPInputStream(response.getEntity.getContent))
        }
        else new java.util.Scanner(response.getEntity.getContent)
    val str = Buffer[String]()
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
    cooky.find { x => x.takeWhile { c => c!='=' }.trim =="cid"}.foreach(s=>cid = s)
    cooky.find { x => x.takeWhile { c => c!='=' }.trim =="__conn"}.foreach(s=>_conn = s)
    
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
  /*
   * value pp: PaintPercentage
   * value pt: PaintTime
   * value doodle: array of strokes
   */
  def submitDoodle(pp:Int,pt:Int,doodle:Array[BasicLine])={//
    val post = new DoodlePost(room,chain,doodle,pp,pt,cookie)
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
    val post = new DescPost(chain,desc,cookie)
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
    cid = ""
    room = "global"
    chain = ""
    auth = new dmodel.JsonSkips
  }
  def login(password:Array[Char],username:String):Boolean={
    
    val post = new LoginPost(password,username,cookie)
    val in = postHttp(post)
    if(in.mkString("\n")!="""<p>Moved Temporarily. Redirecting to <a href="https://doodleordie.com/">https://doodleordie.com/</a></p>""")return false
      //println(in.mkString("\n"))
      //if not ok return false
    //println(_conn)
    val get = new MainGet(cookie)
    val in2 = getHttp(get)
    //println(in2.mkString("\n"))
    //println(_conn)
    cid.length!=0
  }
  def skip{
    val post = new SkipPost(chain,cookie)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
    //???
  }
  def getSkips={
    val get = new SkipsGet(cookie)
    val in = getHttp(get)
    val skips = JsonParse.parseSkips(in.mkString("\n"))
    this.auth = skips
    skips
  }
  def getGroupList={
    val get = new GroupsGet(cookie)
    val in = getHttp(get)
    val list = JsonParse.parseGroupList(in.mkString("\n"))
    if(list.activeGroup!=null&&list.activeGroup._id!=null) room = list.activeGroup._id
    list
  }
  def getDoodle(url:String)={
    val get = new DoodleGet(url,cookie)
    val in = getHttp(get)
    JsonParse.parseDoodle(in.mkString("\n").dropWhile(_!='{').dropRight(2))
  }
  def changeGroup(group_id:String)={
    val post = new ChangeGroupPost(group_id,cookie)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def faceLogin(user:String,pw:Array[Char])={
    false//TODO make facebook login
  }
  def twitterLogin(user:String,pw:Array[Char])={
    val get = new DodGet("auth/twitter?returnTo=%2Fsignin/signin","signin",cookie)
    val in = getHttp(get)
    val inputs = io.AuthParse.parseTwitter(in)
    val post = new TwitterPost(user,pw,inputs,"")
    val in2 = postHttp(post)
    println("HttpHandler twitterLogin")
    println("in2:")
    //println(in2.mkString("\n"))
    val callback = io.AuthParse.parseTwitterCallback(in2)
    if(callback.length()>0){
      println(callback)
      val get2 = new DodGet(callback.drop("http://doodleordie.com/".length),"",cookie)
      //text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8 //        <------
      val in3 = getHttp(get2)
    println("HttpHandler twitterLogin")
    println("in2:")
    //println(in3.mkString("\n"))
    //val get3 = new MainGet(cookie)
    //  getHttp(get3)
    !cid.isEmpty()
    } else false
    //println(in2.mkString("\n"))
    //false//TODO make twitter login work
  }
  def debugGet(ext:String,ref:String)={
    val get = new DefaultGet(ext,ref,cookie)
    val in = getHttp(get)
    println("HttpHandler debugGet")
    println("in:")
    println(in.mkString("\n"))
    println("cookies:")
    println(cookie)
    //
  }
  def hasCid = {
    this.cid!=null&&this.cid.length()>0
  }
  def state={
    val get = new StateGet(cookie)
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
    val post = new ResumePost(cookie)
    val in = postHttp(post)
    JsonParse.parseOk(in.mkString("\n")).isOk
  }
  def ping{
    var f :Future[Boolean] = Future{
      val post = new PingPost(chain,cookie)
      val in = postHttp(post)
      //println(in.mkString("\n, "))
      JsonParse.parseOk(in.mkString("\n")).isOk
    }
    f.onSuccess{
      case b => 
        if(!b) f = Future{
          val post = new PingPost(chain,cookie)
          val in = postHttp(post)
          //println(in.mkString("\n, "))
          JsonParse.parseOk(in.mkString("\n")).isOk
        }
    }
  }
}