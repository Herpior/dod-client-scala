package dmodel

class JsonGroups {
  var groups: Array[JsonGroup] = _
  var staleChain_id:String = _
  var activeGroup: JsonGroup = _
  var isPartial:Boolean = _
  var layout:Boolean = _
  var html:String = _
  def getGroups: Array[JsonGroup] = Array(everyone) ++ groups
}

object everyone extends JsonGroup {
  _id = "global"
  displayName = "Everyone"
}

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

class JsonGroupStats{
  var bannedMembers:Int = _
  var numCompletedChains:Int = _
  var numMembers:Int = _
  var numModerators:Int = _
}