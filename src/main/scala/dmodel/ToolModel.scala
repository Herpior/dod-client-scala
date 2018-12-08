package dmodel
import dmodel.tools._
import view.DoodlePanel

object ToolModel {


  val handTool = new HandTool
  val drawTool = new DrawTool
  val lineTool = new LineTool
  val bezierTool = new BezierTool
  val fillTool = new FillTool
  val perspectiveTool = new PerspectiveTool
  val zoomTool = new ZoomTool
  val bezierInterpolationTool = new BezierInterpolationTool
  val editLineTool = new EditLineTool
  val eraseLineTool = new EraseLineTool

  private var mini = false
  private var state = 0
  private var ready = Magic.readyDefault
  val toolList:Vector[BasicTool] = Vector(
    drawTool,        lineTool,    bezierTool,   fillTool,
    perspectiveTool, zoomTool,    handTool,     bezierInterpolationTool,
    editLineTool, eraseLineTool)
  private var tool:BasicTool = toolList(0)
  
  def isReady = this.ready
  def initReady {ready = Magic.readyDefault}
  
  def setTool(n:Int){
    if(n>=0 && n<=toolList.size && (n!=7 || Magic.authorized)) {
      tool.cleanUp()
      state = n
      tool = toolList(n)
      tool.initTool()
    }
  }
  def getState = state
  def getTool = tool
  
  
  def setReady(rdy:Boolean){
    ready = !ready
  }
  def getReady = ready
  
  def isBusy() = {
    tool.isBusy()
  }
  
  def mouseMoved(dp:DoodlePanel, point:Coord, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseMove(dp, point, ctrl, alt, shift)
  }
  
  def mousePressed(dp:DoodlePanel, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDown(dp, point, button, ctrl, alt, shift)
    /*
      if(doodle.model.isWriting){
        doodle.model.stopWriting
        writing = false
        next = nexttext.toNextLinee
        nexttext = new textLine
        this.addStrooke
        next = new nextLinee
      } else {
        doodle.model.startWriting(place)
        
        writing = true
        val r = addText(e)
        nexttext.cornerx = r._1
        nexttext.cornery = r._2
        setText
        println("click "+nexttext.cornerx)
      }*/
  }
  
  def mouseReleased(dp:DoodlePanel, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseUp(dp, point, button, ctrl, alt, shift)
    /*
     // ^left only = 0
      // ^middle only = 512
      // ^right only = 256
      // left = 1024
      // middle = 2048
      // right = 4096
      // shift = 64
      // ctrl = 128
      // alt =  512
      // alr gr = 640 = alt + ctrl
     */
  }
  
  def mouseDragged(dp:DoodlePanel, point:Coord, left:Boolean, middle:Boolean, right:Boolean, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDrag(dp, point, left, middle, right, ctrl, alt, shift)
  }
}