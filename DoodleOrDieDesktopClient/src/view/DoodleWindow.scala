package view

import scala.swing._
import event.WindowOpened
import scala.swing.event.WindowClosing
import scala.swing.event.UIElementResized
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global

import javax.swing.UIManager

object DoodleWindow extends SimpleSwingApplication {
  
  val ver = "v0.497"
  val version = 497
  
   try {
    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
   } catch {
     case e:Throwable => e.printStackTrace();
   }

  def top = new MainFrame { //TODO: switch to scalafx? http://www.scalafx.org/ what? why? cmon..
    this.title = "Doodle or Die"
    this.iconImage = io.Icons.getDod//new javax.swing.ImageIcon("favicon.png").getImage
    
    this.minimumSize = new Dimension(600, 500)
    this.preferredSize = new Dimension(1100, 800)
    private var screen : WindowPanel =
    if(http.HttpHandler.hasCid){
      new LoadingPanel(Future{
        //http.HttpHandler.getSkips
        val state = http.HttpHandler.state
        if(http.HttpHandler.loggedIn)
        state.toPlayPanel
        else new LogonPanel},new LogonPanel)
    } else new LogonPanel
    //val condo = new CondimentPanel
    this.contents = screen//new BoxPanel(Orientation.Horizontal){}
      /*contents += new BoxPanel(Orientation.Vertical){
        this.maximumSize = new Dimension(1040,780)
        this.preferredSize = this.maximumSize
        this.contents += new ScrollPane(new DrawingPanel(condo))
      }*///contents += new ScrollPane(new DrawingPanel(condo))
      /*contents += new BoxPanel(Orientation.Vertical){
        this.maximumSize = new Dimension(300,2000)
        //this.preferredSize = this.maximumSize
        this.contents += new ScrollPane(condo)
      }
    }
    */
    this.listenTo(screen)
    reactions +={
      case e:UIElementResized=>
        this.preferredSize = this.bounds.getSize
      case e:ReplaceEvent=>
        screen = e.replacement
        this.contents = screen
        //println("replaced should this now be: "+screen.getClass)
        screen.repaint()
        this.listenTo(screen)
      case e:WindowClosing =>  //println(e.getClass)
        try{
          if(screen.isInstanceOf[DoodlingPanel]){
            screen.asInstanceOf[DoodlingPanel].save
          }
        }
        finally{
          System.exit(0)
        }
        
    }
    //println(this.bounds)
    this.centerOnScreen()
  }
  
}