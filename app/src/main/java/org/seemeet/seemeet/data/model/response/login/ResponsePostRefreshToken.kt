package org.seemeet.seemeet.data.model.response.login

data class ResponsePostRefreshToken(
    val data: TokenData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class TokenData(
    val accessToken: String,
    val refreshToken: String
)
