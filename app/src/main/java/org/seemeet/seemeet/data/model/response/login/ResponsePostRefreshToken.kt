package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName

data class ResponsePostRefreshToken(
    val data: TokenData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class TokenData(
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
)
