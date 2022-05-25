package org.seemeet.seemeet.data.model.response.login

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: ExUser,
    val accesstoken: String
)