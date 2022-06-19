package org.seemeet.seemeet.data.model.response.register

import org.seemeet.seemeet.data.model.response.login.Accesstoken
import java.util.*

data class ResponseRegisterList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: UserInfo,
    val accesstoken: Accesstoken
)

data class UserInfo(
    val id: Int,
    val email: String,
    val idFirebase: String,
    val username: String,
    val isNoticed: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
    val isDeleted: Boolean,
    val provider: String,
    val socialId: String,
    val nickname: String
)