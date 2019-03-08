package dmodel.json

/**
  * Json class for gson to load step content data in step data in profile data from the servers
  *

  * @author Qazhax
  */

class JsonStepContent {
  // doodle data
  var version:Int = 1 // if left at 1, probably no json file
  var doodle_id:String = _
  var user_id:String = _
  var date:String = _
  var time:Int = _
  var count:Int = _
  var width:Int = _
  var height:Int = _
  var ext:String = _
  var url:String = _
  var jsonp:Boolean = _ // if not set to true, the .js file in url is actually png
  // phrase data
  var phrase:String = _
  // old doodle data
  var dataLength:String = _ //idk what?
  var paintPercentage:Int = _
  var paintTime:Int = _ //renamed to time
  var strokeCount:Int = _ //renamed to count
}
/*
*
        "content": {
          "version": 2,
          "doodle_id": "nR320VPqv",
          "user_id": "Ei8sOGPaE",
          "date": "2019-03-04T05:30:00.377Z",
          "time": 282433,
          "count": 45,
          "width": 520,
          "height": 390,
          "ext": "userscript",
          "url": "https://doodleordie.s3.amazonaws.com/d/Ei8sOGPaE/nR320VPqv.js",
          "jsonp": true
        },

      "content": {
        "dataLength": "54696",
        "height": 390,
        "paintPercentage": 27,
        "paintTime": 91994,
        "strokeCount": 37,
        "url": "https://doodleordie.s3.amazonaws.com/d/Ei8sOGPaE/11__15wJrl65.js",
        "width": 520
      },
        "content": {
          "phrase": "A little purple beetle who is a hippie and has seen some shit."
        }
* */