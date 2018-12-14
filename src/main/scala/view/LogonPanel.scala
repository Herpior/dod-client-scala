package view

import java.net.URI

import scala.swing._
//import Swing.Embossing
import BorderPanel.Position._
import java.awt.Font
import java.awt.Color
import dmodel.Magic
import scala.swing.event._
import http.HttpHandler
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global

class LogonPanel extends /*BoxPanel(Orientation.Vertical)*/FlowPanel with WindowPanel{
  //layout(new BoxPanel(Orientation.Horizontal)) = North
  //layout(new FlowPanel) = South
  //layout(new FlowPanel) = East
  //layout(new FlowPanel) = West
  /*val test = new ColourPickerFrame(Magic.bgColor)(test2)
  def test2(x:Color){println(dmodel.Colors.toHexString(x))}
  test.open()*/
  
  private val sign = new SignonPanel(this)
  this.listenTo(sign)
  this.listenTo(this.keys)
  this.listenTo(sign.keys)
  //this.listenTo(sign.autoButt.keys)
  //this.listenTo(sign.unField.keys)
  this.reactions +={
    /*case e:KeyPressed if e.key == Key.T =>
      http.HttpHandler.twitterGet()
    case e:KeyPressed if e.key == Key.N =>
          val text = Dialog.showInput(this, "url extension", "open", Dialog.Message.Question, null, List[String](), "http://doodleordie.com/")
          text.foreach { x => 
            val ref = Dialog.showInput(this, "ref", "open", Dialog.Message.Question, null, List[String](), "http://doodleordie.com/")
            ref.foreach { y =>
              http.HttpHandler.debugGet(x,y)
            }
          }*/
    case e:ReplaceEvent =>
      if(e.source==sign)this.publish(new ReplaceEvent(e.replacement,this))
  }
  def continue = sign.continue
  val bordersize = new Dimension(300,80)
      this.background = Magic.bgColor
  
  //contents += new ScrollPane{contents=new BoxPanel(Orientation.NoOrientation){this.preferredSize=bordersize
  //    this.background = Magic.bgColor}}
  contents += new BoxPanel(Orientation.Vertical){
      this.background = Magic.bgColor
    this.contents += new BoxPanel(Orientation.NoOrientation){
      this.minimumSize = new Dimension(0,0)
      this.preferredSize=new Dimension(100,40)
      this.maximumSize=bordersize
      this.background = Magic.bgColor}
    this.contents+=new BorderPanel{
      this.background = Magic.buttColor
      val bord = Swing.EmptyBorder(1,1,1,1)
      this.border = bord
      layout(sign) = Center
      }
    this.contents += new BoxPanel(Orientation.NoOrientation){this.preferredSize=bordersize
      this.background = Magic.bgColor}
    }
  //contents += new ScrollPane{contents=new BoxPanel(Orientation.NoOrientation){this.preferredSize=bordersize
  //    this.background = Magic.bgColor}}
  
  //layout(new Label("Username")) = Center
  //layout(new TextField) = Center
  //layout(new Label("Password")) = Center
  //layout(new TextField) = Center
  
}
class SignonPanel(owner:WindowPanel) extends BoxPanel(Orientation.Vertical){
  private var status = 0
  this.border =  Swing.EmptyBorder(30,30,30,30)//new Border
  this.background = Color.WHITE
  this.preferredSize = new Dimension(600,600)
  this.minimumSize = new Dimension(300,300)
  this.maximumSize = new Dimension(600,800)
  //private val font20 = Magic.font20
  val unField = new TextField
  val autoButt = new RadioButton("Sign in automatically"){
    this.background = Magic.white
  }
  /*
  val twitterButt = new LoginButton("Sign in with Twitter"){
    this.action=Action("Sign in with Twitter"){
      switchToTwitter
    }
  }
  val faceButt = new LoginButton("Sign in with Facebook"){
    this.action=Action("Sign in with Facebook"){
      switchToFace
    }
  }
  */
  val registerButt =  new LoginButton("Register"){
    this.action=Action("Register"){
      launchDodRegistration
    }
  }
  val normalButt = new LoginButton("Sign in normally"){
    this.action=Action("Sign in normally"){
      switchToNormal
    }
  }
  val cookieButt = new LoginButton("Sign in with a cookie"){
    this.action=Action("Sign in with a cookie"){
      switchToCookie
    }
  }
  val singButt = new LoginButton("Sign In"){
    this.action=Action("Sign in"){
      continue
    }
  }
  val offButt = new LoginButton("Offline Mode"){
    this.action=Action("Offline Mode"){
      offline
    }
  }
  val unLabel = new Label("Username"){this.font = Magic.font20}
  val pwLabel = new Label("Password"){this.font = Magic.font20}
  val sigLabel = new Label("Sign in"){this.font = Magic.font20}
  this.contents += sigLabel
  this.contents += new Label(" "){this.font = Magic.font20}
  //this.contents += faceButt
  //this.contents += twitterButt
  this.contents += registerButt
  this.contents += cookieButt
  //this.contents += new Label(" "){this.font = Magic.font20}
  this.contents += unLabel
  unField.preferredSize = new Dimension(900, 50)
  unField.maximumSize = new Dimension(1900, 50)
  unField.font = Magic.font20
  this.contents += unField
  this.contents += new Label(" "){this.font = Magic.font20}
  this.contents += pwLabel
  val pwField = new PasswordField
  pwField.font = Magic.font20//.deriveFont(40f)
  pwField.preferredSize = new Dimension(900, 50)
  pwField.maximumSize = new Dimension(1800, 50)
  this.contents += pwField
  this.contents += new Label(" "){this.font = Magic.font20}
  this.contents += singButt
  this.contents += autoButt
  this.contents += offButt

  /*
  def switchToTwitter {
    val index = this.contents.indexOf(twitterButt)
    //this.contents -= twitterButt
    if(index>0 && status != 1){
      this.contents(index) = status match{
        case 0 => normalButt
        case 3 => cookieButt
        case _ => faceButt
      }
      sigLabel.text = "Sign in with Twitter"
      unLabel.text = "Username or e-mail"
      pwLabel.text = "Password"
      status = 1
      this.revalidate()
      this.repaint()
    }
  }
  def switchToFace{
    val index = this.contents.indexOf(faceButt)
    //this.contents -= twitterButt
    if(index>0 && status != 2){
      this.contents(index) = status match{
        case 0 => normalButt
        case 3 => cookieButt
        case _ => twitterButt
      }
      sigLabel.text = "Sign in with Facebook"
      unLabel.text = "E-mail"
      pwLabel.text = "Password"
      status = 2
      this.revalidate()
      this.repaint()
    }
  }
  */
  def switchToCookie{
    val index = this.contents.indexOf(cookieButt)
    //this.contents -= twitterButt
    if(index>0 && status != 3){
      this.contents(index) = status match{
        //case 0 => normalButt
        //case 1 => twitterButt
        //case 2 => faceButt
        case _ => normalButt
      }
      sigLabel.text = "Sign in with a cookie"
      unLabel.text = "just leave this empty"
      pwLabel.text = "Authentication cookie"
      status = 3
      this.revalidate()
      this.repaint()
    }
  }
  def launchDodRegistration: Unit ={
    import java.awt.Desktop
    val url = "https://doodleordie.com/create-account"

    if (Desktop.isDesktopSupported) { // Windows
      Desktop.getDesktop.browse(new URI(url))
    }
    else { // Ubuntu
      val runtime = Runtime.getRuntime
      runtime.exec("/usr/bin/firefox -new-window " + url)
    }
  }
  def switchToNormal{
    val index = this.contents.indexOf(normalButt)
    //this.contents -= twitterButt
    if(index>0 && status != 0){
      this.contents(index) = status match{
        //case 1 => twitterButt
        //case 2 => faceButt
        case _ => cookieButt
      }
      sigLabel.text = "Sign in"
      unLabel.text = "Username"
      pwLabel.text = "Password"
      status = 0
      this.revalidate()
      this.repaint()
    }
  }
  def offline {
    val next = new LoadingPanel(Future{
        Magic.offline = true;
        ConfigDrawingPanel.activate
        new DoodlingPanel("offline", "offline", "Offline", false)
        },owner)
    val e = new ReplaceEvent(next,this)
      this.publish(e)
  }
  def continue {
    //println(HttpHandler.cook)
/*      val next = new LoadingPanel(Future(HttpHandler.state.toPlayPanel),owner)
      val e = new ReplaceEvent(next,this)
      this.publish(e)
    } else {*/
      if((unField.text.length()==0 && status < 3) || pwField.password.length==0) {
        Dialog.showMessage(owner, "Username or password field empty", "failed to login", Dialog.Message.Warning, null)
        return
      }
      val next = new LoadingPanel(Future{
        val success = status match {
          case 0 =>
            try {
              HttpHandler.login(pwField.password, unField.text)
            } catch {
            case e:javax.net.ssl.SSLHandshakeException=> 
                Dialog.showMessage(owner, "The SSL cert is probably expired, login using a cookie", "failed to login", Dialog.Message.Warning, null)
                false
            }
          //case 1 =>
          //  HttpHandler.twitterLogin(unField.text, pwField.password)
          //case 2 =>
           // HttpHandler.faceLogin(unField.text, pwField.password)
          case _ =>
            HttpHandler.cookieLogin(pwField.password)
        }
        if(success && HttpHandler.hasCid){
          if(this.autoButt.selected){
            HttpHandler.saveCid
          }
          val state = HttpHandler.state
          state.toPlayPanel
        } else {
          Dialog.showMessage(owner, "most likely an error in username or password", "failed to login", Dialog.Message.Warning, null)
          owner
        }
        },owner)
      val e = new ReplaceEvent(next,this)
      this.publish(e)
    //}
    //println(HttpHandler.cook)
  }
  
  //this.listenTo(this.keys)
  this.listenTo(unField.keys)
  this.listenTo(this.singButt.keys)
  this.listenTo(this.pwField.keys)
  reactions += {
    
    case e:KeyPressed if e.key == Key.Enter =>
      continue
  }
  
  controller.Timer(100,false){
  unField.requestFocusInWindow()
  }.start
  
  
}

/*class PasswordFieldo extends PasswordField{
   /* private var pwtext = ""
    def clear = {
      this.pwtext = ""
      this.text = ""
    }
    def pw = pwtext
    this.listenTo(this.keys)
    this.reactions += {
      case e:KeyTyped=>
        //val ind = this.caret.position
        //if(e.char!=''&&e.char!=''){
          val sel = this.selected
          if(sel!=null&&sel.length == this.text.length){
            pwtext = ""
            //text = ""
            //this.caret.position = 0
          }
        //}
      case e:KeyReleased=>
        val ind = this.caret.position
        val changed = this.text.length-this.pwtext.length
        println(changed)
        if(e.key==Key.BackSpace&&ind>=0){
          pwtext = pwtext.take(ind)+pwtext.drop(ind-changed)
          if(this.selected!=null&&this.selected.length == this.text.length)pwtext = ""
        } else if(e.key==Key.Delete&&ind<this.pwtext.length){
          pwtext = pwtext.take(ind+changed)+pwtext.drop(ind+1)
          if(this.selected!=null&&this.selected.length == this.text.length){
            pwtext = ""
          }
        }
          else{
          pwtext = pwtext.take(ind)+this.text.drop(ind).take(changed)+pwtext.drop(ind)
          }
       // this.text = "*"*(math.max(pwtext.length,0)) 
        println(pwtext)
        this.caret.position=ind
    }*/
}*/