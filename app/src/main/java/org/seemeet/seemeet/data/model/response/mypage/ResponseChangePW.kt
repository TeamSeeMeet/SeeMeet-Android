package org.seemeet.seemeet.data.model.response.mypage

import org.seemeet.seemeet.data.model.response.login.ExUser

data class ResponseChangePW(
    val data: List<ExUser>,
    val status: Int,
    val success: Boolean,
    val message: String
)