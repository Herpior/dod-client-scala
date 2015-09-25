package view

import scala.swing._
import event.WindowOpened
import scala.swing.event.WindowClosing
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global

object DoodleWindow extends SimpleSwingApplication {
  
  val ver = "v0.400"

  def top = new MainFrame {
    this.title = "Doodle or Die"
    this.iconImage = io.Icons.getDod//new javax.swing.ImageIcon("favicon.png").getImage
    
    this.minimumSize = new Dimension(600, 500)
    this.preferredSize = new Dimension(1100, 800)
    private var screen : WindowPanel =
    if(http.HttpHandler.hasCid){
      new LoadingPanel(Future{
        //http.HttpHandler.getSkips
        val state = http.HttpHandler.state
        if(http.HttpHandler.getAuth)
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
      case e:ReplaceEvent=>
        screen = e.replacement
        this.contents = screen
        //println("replaced should this now be: "+screen.getClass)
        screen.repaint()
        this.listenTo(screen)
      case e:WindowClosing =>  //println(e.getClass)
        try{
          if(screen.isInstanceOf[DoodlingPanel]){
            screen.asInstanceOf[DoodlingPanel].doodle.save
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