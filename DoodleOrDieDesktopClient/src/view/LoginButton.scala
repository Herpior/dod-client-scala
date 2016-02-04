package view

import swing.Button
import dmodel.Magic
import swing.Dimension
import java.awt.Color
/**
 * @author Herpior
 */
class LoginButton(text:String) extends Button(text){
    this.background = Magic.buttColor
    this.preferredSize = new Dimension(900, 40)
    this.maximumSize = new Dimension(1900, 40)
    this.contentAreaFilled = true
    this.minimumSize = this.preferredSize
    this.font = Magic.font20
    this.foreground = Color.WHITE
}