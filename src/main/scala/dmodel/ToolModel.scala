package dmodel
import dmodel.tools._

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
  
  def isReady: Boolean = this.ready
  def initReady() {ready = Magic.readyDefault}
  
  def setTool(n:Int){
    if(n>=0 && n<=toolList.size && (n!=7 || Magic.authorized)) {
      tool.cleanUp()
      state = n
      tool = toolList(n)
      tool.initTool()
    }
  }
  def getState: Int = state
  def getTool: BasicTool = tool
  
  
  def setReady(rdy:Boolean){
    ready = !ready
  }
  def getReady: Boolean = ready
  
  def isBusy = {
    tool.isBusy
  }
  
  def mouseMoved(db:DoodleBufferer, point:Coord, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseMove(db, point, ctrl, alt, shift)
  }
  
  def mousePressed(db:DoodleBufferer, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDown(db, point, button, ctrl, alt, shift)
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
  
  def mouseReleased(db:DoodleBufferer, point:Coord, button:Int, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseUp(db, point, button, ctrl, alt, shift)
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
  
  def mouseDragged(db:DoodleBufferer, point:Coord, left:Boolean, middle:Boolean, right:Boolean, ctrl:Boolean, alt:Boolean, shift:Boolean) {
    tool.onMouseDrag(db, point, left, middle, right, ctrl, alt, shift)
  }
}