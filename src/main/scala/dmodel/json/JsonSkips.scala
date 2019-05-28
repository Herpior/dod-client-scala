package dmodel.json

/**
  * Json class for gson to load skip data from the servers.
  * it is also the easiest way to check for super privileges, because the server sets skip amount per duration to 1000 for supers.

  * @author Qazhax
  */

class JsonSkips {
  var skipsAvailable:Int=_
  var skipDurationHours:Int=_
  var skipsPerDuration:Int=0
  
  def isSuper: Boolean = {
    skipsPerDuration==1000 //very weird way to figure out if super
  }
  def isLoggedIn: Boolean = {
    skipsPerDuration>0 //very weird way to figure out if logged in
  }
}