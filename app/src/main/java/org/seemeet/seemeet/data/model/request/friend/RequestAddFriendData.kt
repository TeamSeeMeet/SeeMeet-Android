package org.seemeet.seemeet.data.model.request.friend

import com.google.gson.annotations.SerializedName

data class RequestAddFriendData(

    @SerializedName("nickname")
    val nickname: String
)