package dmodel.json

/**
  * Json class for gson to load response data from the servers.
  * Sometimes the server returns ok:true, but adds a valid:false to the response,
  * so it also needs to check that valid is not false.

  * @author Qazhax
  */

class JsonOk{
  var ok:Boolean = _
  var valid:Boolean = true
  def isOk: Boolean = {
    ok && valid
  }
}
