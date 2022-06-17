package org.seemeet.seemeet.data.model.request.friend

import com.google.gson.annotations.SerializedName

data class RequestUserData(

    @SerializedName("nickname")
    val nickname: String
)