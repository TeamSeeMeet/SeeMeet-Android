package org.seemeet.seemeet.data.local

data class NotificationInProgressData(
    val day : String,
    val requestTitle : String,
    val msg : String,
    val viewType : Int,
    val nameList : List<NotificationFriendData>,
)