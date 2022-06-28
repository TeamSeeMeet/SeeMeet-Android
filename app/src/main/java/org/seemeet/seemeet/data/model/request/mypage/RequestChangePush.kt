package org.seemeet.seemeet.data.model.request.mypage

data class RequestChangePush(
    val push: Boolean,
    val fcm: String
)
