package view

import scala.swing._
import dmodel.Magic
import java.awt.Font
import http.HttpHandler
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global

class StalingPanel extends BoxPanel(Orientation.Vertical) with PlayPanel{
  this.background = Magic.bgColor
  val bordersize = new Dimension(300,80)
  this.contents += new BoxPanel(Orientation.NoOrientation){
    this.preferredSize=bordersize
    this.background = Magic.bgColor}
  val showButt = new Button{
    this.background = Magic.buttColor
    this.foreground = Magic.white
    this.opaque = true
    this.borderPainted = false
    this.contentAreaFilled = true
    //this.text = "Show me the next chain!"
    this.action = Action("Show me the next chain!"){
      resume
    }
  }
  
  
  def resume = {
    this.publish(
      new ReplaceEvent(
        new LoadingPanel(
          Future{
            if(HttpHandler.resume){
              HttpHandler.state.toPlayPanel
            } else {
              //println("failed to resume")
              this
            }
          },this
        ),this
      )
    )
  }
  def save {}
  
  /*private def replaceWith(play:PlayPanel){
    this.publish(new ReplaceEvent(play,this))
  }*/
  
  showButt.font = Magic.font20
  val descrip = new PhrasePanel{
    this.preferredSize = new Dimension(900,150)
    this.desc.contents += new FlowPanel(roomChanger,showButt,logoutButt){this.background=Magic.white}
  }
  //desc.font = font20
  descrip.setPhrase("Ready?")
  this.contents += new FlowPanel(descrip){this.background=Magic.bgColor}
/*
  def submit={
    ???
    true
  }
  def load={
    ???
    true
  }
  def skip={
    ???
    true
  }*/
}