package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName
import org.seemeet.seemeet.data.model.response.register.UserInfo

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: UserInfo,
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
)
