package io

/**
 * @author Herpior
 */

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.io.IOException
import javax.swing.ImageIcon
import java.awt.Toolkit

object Icons {
  val tk = Toolkit.getDefaultToolkit()
  val pen = tk.getImage(getClass.getResource("/images/pencil.png"))
  val line = tk.getImage(getClass.getResource("/images/line.png"))
  val fill = tk.getImage(getClass.getResource("/images/fill.png"))
  val bezier = tk.getImage(getClass.getResource("/images/bezier.png"))
  val bezierfill = tk.getImage(getClass.getResource("/images/bezierfill.png"))
  val colorpicker = tk.getImage(getClass.getResource("/images/colorpicker.png"))
  val colorinjector = tk.getImage(getClass.getResource("/images/colorinjector.png"))
  val eraser = tk.getImage(getClass.getResource("/images/eraser.png"))
  val hand = tk.getImage(getClass.getResource("/images/hand6.png"))
  val zoom = tk.getImage(getClass.getResource("/images/zoom2.png"))
  val pers = tk.getImage(getClass.getResource("/images/pers.png"))
  val eye = tk.getImage(getClass.getResource("/images/eye.png"))
  val check = tk.getImage(getClass.getResource("/images/check.png"))
  //val skip = new ImageIcon(tk.getImage(getClass.getResource("/skip.png")))
  val dod = tk.getImage(getClass.getResource("/images/favicon.png"))
  //val pentest1 = getClass.getClassLoader.getResource("/pencil.png")
  //val pentest2 = getClass.getClassLoader.getResource("/resources/pencil.png")
  //val pentest3 = getClass.getClassLoader.getResource("/main/resources/pencil.png")
  //val pentest4 = getClass.getResource("pencil.png")
  //val pentest5 = getClass.getResource("/pencil.png")
  //println("found: " +(pentest1!=null).toString +" "+ (pentest2!=null).toString + " " + (pentest3!=null).toString + " " + (pentest4!=null).toString + " " + (pentest5!=null).toString);
  //println(new java.io.File("/pencil.png").getAbsolutePath())
  //println(getClass().getResource(getClass().getSimpleName() + ".class"))
  /*private var pencilIcon = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)
  private var lineIcon = pencilIcon
  private var fillIcon = pencilIcon
  private var bezierIcon = pencilIcon
  private var persIcon = pencilIcon
  private var eyeIcon = pencilIcon
  private var checkIcon = pencilIcon
  private var skipIcon = new ImageIcon(this.pencilIcon)*/
  private var penCursorIcon = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)//pencilIcon
  
  /*try{
    pencilIcon = ImageIO.read(new File("pencil.png"));
  } catch {
    case e:IOException =>
  }
  try{
    lineIcon = ImageIO.read(new File("line.png"));
  } catch {
    case e:IOException =>
  }
  try{
    fillIcon = ImageIO.read(new File("fill.png"));
  } catch {
    case e:IOException =>
  }
  try{
    bezierIcon = ImageIO.read(new File("bezier.png"));
  } catch {
    case e:IOException =>
  }
  try{
    persIcon = ImageIO.read(new File("pers.png"));
  } catch {
    case e:IOException =>
  }
  try{
    eyeIcon = ImageIO.read(new File("eye.png"));
  } catch {
    case e:IOException =>
  }
  try{
    checkIcon = ImageIO.read(new File("check.png"));
  } catch {
    case e:IOException =>
  }
  try{
    skipIcon = new ImageIcon(ImageIO.read(new File("skip.png")));
  } catch {
    case e:IOException =>
  }*/
  
  def getPen = pen//this.pencilIcon
  def getLine = line//this.lineIcon
  def getFill = fill//this.fillIcon
  def getBez = bezier//this.bezierIcon
  def getDrag = hand//this.bezierIcon
  def getZoom = zoom//this.bezierIcon
  def getBezFill = bezierfill//this.bezierIcon
  def getPers = pers//this.persIcon
  def getColorPicker = colorpicker//this.persIcon
  def getColorInjector = colorinjector//this.persIcon
  def getEraser = eraser//this.persIcon
  //def getSkip = skip//this.skipIcon
  def getVisible = eye//this.eyeIcon
  def getCheck = check//this.checkIcon
  def getDod = dod//this.checkIcon
  def getPenCursor = this.penCursorIcon
}