package dmodel.json

/**
  * Json class for gson to load the step data in profile data from the servers
  *

  * @author Qazhax
  */

class JsonStep {
  var _id:String = _
  var chain_id:String = _
  var content:JsonStepContent = _
  var date:String = _
  var group_id:String = _
  //var interactions:JsonStepInteractions = _
  var player_id:String = _
  var previousStep_id:String = _
  var state:String = _
  //var stats:JsonStepStats = _
  var stepIndex:Int = _
  var numLikes:Int = _
  var groupDisplayName:String = _
  var showGroupLink:String = _
  /*
      "interactions": {
        "like": {
          "1hw40d67kE": {
            "date": "2014-04-05T21:21:22.499Z",
            "notification_id": "ly134L8V2__n0"
          },
          "8qEA80BsW1": {
            "date": "2014-04-05T21:23:00.040Z",
            "notification_id": "lJAdOULEn__n0"
          },
          "g14gn0-qu": {
            "date": "2014-05-17T03:05:13.608Z",
            "notification_id": "gyUcZW8U0__n0"
          }
        }

        "stats": {
          "numLikes": 3
        },
  * */
}
