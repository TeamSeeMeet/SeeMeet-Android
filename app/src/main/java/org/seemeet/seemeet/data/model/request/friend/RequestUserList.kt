package org.seemeet.seemeet.data.model.request.friend

import com.google.gson.annotations.SerializedName

data class RequestUserList(

    @SerializedName("email")
    val email: String
)