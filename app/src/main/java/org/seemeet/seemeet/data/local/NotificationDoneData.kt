package org.seemeet.seemeet.data.local

data class NotificationDoneData(
    val day : String,
    val isCanceled : Boolean,
    val appointmentTitle : String,
    val nameList : List<NotificationFriendData>,
) {
    data class DetailData(
        val date : String,
        val time : String,
        val title : String,
        val contents : String,
    )
}

