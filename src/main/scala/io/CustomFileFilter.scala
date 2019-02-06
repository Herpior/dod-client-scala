package io

import javax.swing.filechooser.FileFilter
import java.io.File

class PngFilter extends FileFilter {

  def accept(file:File): Boolean = {
    file.getName.toLowerCase().endsWith("png") || file.isDirectory
  }
  def getDescription: String = {
    "png file"
  }
}


class TxtFilter extends FileFilter {

  def accept(file:File): Boolean = {
    if(file.getName.toLowerCase().endsWith("txt") && file.canRead) {
      val ensource = scala.io.Source.fromFile(file)("UTF-8")
      try{
        ensource.take(3).mkString == "{\"v" //the save files should all start like this so this will hide most of the other files
      } catch {
        case e:Throwable => false
      }
    }
    else file.isDirectory
  }
  def getDescription: String = {
    "savefile"
  }
}