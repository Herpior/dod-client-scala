package dmodel

object ToolModel {

  private var mini = false
  private var state = 0
  private var ready = Magic.readyDefault
  
  def isReady = this.ready
  def initReady {ready = Magic.readyDefault}
  
  def tool(n:Int){
    if(n<2||Magic.authorized)
    state=n
  }
  def getState = state
  
  
  def clickReady(){
    ready = !ready
  }
}