package org.seemeet.seemeet.data.model.response.register

import com.google.gson.annotations.SerializedName

data class ResponseRegisterList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val newUser: UserInfo,
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
)

data class UserInfo(
    val id: Int,
    val email: String?,
    val idFirebase: String?,
    val username: String,
    val isNoticed: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean,
    val provider: String,
    val socialId: String?,
    val nickname: String?,
    val imgLink: String?,
    val push: Boolean,
    val password: String,
    val imgName: String?,
    val fcm: String?
)