package dmodel

/**
  * Json class for gson to load group data from the servers
  *

  * @author Qazhax
  */

class JsonGroup {
  var _id:String = _
  var displayName:String = _
  var stats:JsonGroupStats = _
  var urlSafe:String = _
  override def toString: String ={
    displayName
  }
  def change: Boolean = {
    http.HttpHandler.changeGroup(_id)
  }
}
