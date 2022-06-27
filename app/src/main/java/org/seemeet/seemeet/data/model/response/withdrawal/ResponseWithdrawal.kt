package org.seemeet.seemeet.data.model.response.withdrawal

import org.seemeet.seemeet.data.model.response.login.Accesstoken
import org.seemeet.seemeet.data.model.response.login.ExUser

data class ResponseWithdrawal(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: ExUser,
    val accesstoken: Accesstoken
)