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
    val date : String,
    val time : String,
    val flag : Boolean
)
data class ReceiverData(
    val name : String,
    val response : Boolean
)
