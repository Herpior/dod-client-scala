package dmodel

/**
  * Json class for gson to load skip data from the servers.
  * it is also the easiest way to check for super privileges, because the server sets skip amount to 1000 for supers.

  * @author Qazhax
  */

class JsonSkips {
  var skipsAvailable:Int=_
  var skipDurationHours:Int=_
  var skipsPerDuration:Int=_
  
  def isSuper: Boolean = {
    skipsPerDuration==1000
  }
}