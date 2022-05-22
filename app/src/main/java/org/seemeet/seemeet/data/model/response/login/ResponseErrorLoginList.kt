package org.seemeet.seemeet.data.model.response.login

data class ResponseErrorLoginList(
    val status: Int,
    val success: String,
    val code: Int,
    val message: String
)
