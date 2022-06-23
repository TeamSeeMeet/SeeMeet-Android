package org.seemeet.seemeet.data.model.response.mypage

import org.seemeet.seemeet.data.model.response.login.ExUser

data class ResponseMyPageProfile(
    val data: ExUser,
    val status: Int,
    val success: Boolean
)