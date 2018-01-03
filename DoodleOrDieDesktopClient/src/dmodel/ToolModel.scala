package dmodel
import dmodel.tools._

object ToolModel {

  private var mini = false
  private var tool:BasicTool = DrawTool
  private var state = 0
  private var ready = Magic.readyDefault
  
  def isReady = this.ready
  def initReady {ready = Magic.readyDefault}
  
  def tool(n:Int){
    if(n<2||Magic.authorized)
    state=n
    //TODO: change this part completely, to use the tool objects
  }
  def getState = state
  def getTool = tool
  
  
  def clickReady(){
    ready = !ready
  }
}