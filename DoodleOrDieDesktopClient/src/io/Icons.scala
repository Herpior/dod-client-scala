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
  val pen = tk.getImage(getClass().getResource("/pencil.png"))
  val line = tk.getImage(getClass().getResource("/line.png"))
  val fill = tk.getImage(getClass().getResource("/fill.png"))
  val bezier = tk.getImage(getClass().getResource("/bezier.png"))
  val bezierfill = tk.getImage(getClass().getResource("/bezierfill.png"))
  val pers = tk.getImage(getClass().getResource("/pers.png"))
  val eye = tk.getImage(getClass().getResource("/eye.png"))
  val check = tk.getImage(getClass().getResource("/check.png"))
  //val skip = new ImageIcon(tk.getImage(getClass().getResource("/skip.png")))
  val dod = tk.getImage(getClass().getResource("/favicon.png"))
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
  def getBezFill = bezierfill//this.bezierIcon
  def getPers = pers//this.persIcon
  //def getSkip = skip//this.skipIcon
  def getVisible = eye//this.eyeIcon
  def getCheck = check//this.checkIcon
  def getDod = dod//this.checkIcon
  def getPenCursor = this.penCursorIcon
}