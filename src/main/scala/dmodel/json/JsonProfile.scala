package dmodel.json

/**
  * Json class for gson to load profile data from the servers
  *

  * @author Qazhax
  */

class JsonProfile {
  var collection:Array[JsonProfileStep] = Array()
  var isViewerAdmin:Boolean = _
  var isViewingOwnProfile:Boolean = _
  var page:Int = _
  var per_page:Int = _
  var category:String = _
  var sort:String = _
  var loadMore:String = _
}
