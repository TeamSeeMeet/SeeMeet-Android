package org.seemeet.seemeet.data.model.request.friend

import com.google.gson.annotations.SerializedName

data class RequestUserData(

    @SerializedName("email")
    val email: String
)