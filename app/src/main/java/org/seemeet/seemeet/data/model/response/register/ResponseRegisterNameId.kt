package org.seemeet.seemeet.data.model.response.register


data class ResponseRegisterNameId(
    val data: UserInfo,
    val status: Int,
    val message: String,
    val success: Boolean
)