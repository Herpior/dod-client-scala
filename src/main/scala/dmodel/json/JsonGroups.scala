package dmodel.json

/**
  * Json class for gson to load group data from the servers
  *
  * @author Qazhax
  */

class JsonGroups {
  var groups: Array[JsonGroup] = _
  var staleChain_id:String = _
  var activeGroup: JsonGroup = _
  var isPartial:Boolean = _
  var layout:Boolean = _
  var html:String = _
  def getGroups: Array[JsonGroup] = Array(everyone) ++ groups
}

// Everyone room that everyone has access to, not part of the groups list from server
object everyone extends JsonGroup {
  _id = "global"
  displayName = "Everyone"
}

