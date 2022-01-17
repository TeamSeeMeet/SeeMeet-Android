package org.seemeet.seemeet.data.local

data class NotificationDoneData(
    val day : String,
    val confirmOrDelete : String,
    val appointmentTitle : String,
    val nameList : List<NotificationFriendData>
)

