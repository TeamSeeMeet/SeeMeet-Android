package org.seemeet.seemeet.data.local

data class ReceiveData(
    val request : String,
    val recieveList : List<String>,
    val title : String,
    val content : String,
    val times : List<String>,
    val flag : Boolean
)
data class CheckboxData(
    val id : Int,
    val date : String,
    val time : String,
    var flag : Boolean
)
data class ReceiverData(
    val name : String,
    val response : Boolean
)

data class ScheduleData(
    val id : Int,
    val date: String,
    val title : String,
    val together : List<String>,
    val time : String
)