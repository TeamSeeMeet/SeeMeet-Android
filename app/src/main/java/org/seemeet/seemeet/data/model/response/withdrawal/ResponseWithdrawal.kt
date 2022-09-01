package org.seemeet.seemeet.data.model.response.withdrawal

import org.seemeet.seemeet.data.model.response.register.UserInfo

data class ResponseWithdrawal(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: UserInfo
)