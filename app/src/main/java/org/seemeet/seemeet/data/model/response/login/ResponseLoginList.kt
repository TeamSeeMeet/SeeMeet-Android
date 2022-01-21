package org.seemeet.seemeet.data.model.response.login

import java.util.*

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class UserInfo(
    val id: Int,
    val email: String,
    val idFirebase: String,
    val username: String,
    val gender: String,
    val birth: Date,
    val isNoticed: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
    val isDeleted: Boolean
)

data class Data(
    val user: UserInfo,
    val accesstoken: String
)