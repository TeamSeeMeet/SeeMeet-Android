package org.seemeet.seemeet.data.model.request.login

import com.google.gson.annotations.SerializedName

data class RequestKakaoLogin(
    @SerializedName("socialtoken")
    val socialToken: String,
    val provider: String,
    val fcm : String
)
