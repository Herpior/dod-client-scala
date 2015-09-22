package dmodel

/**
 * @author Herpior
 */
class JsonSkips {
  var skipsAvailable:Int=_
  var skipDurationHours:Int=_
  var skipsPerDuration:Int=_
  
  def isSuper = {
    skipsPerDuration==1000
  }
}