package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: ExUser,
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
)