package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

//친구 목록 요청
data class ResponseFriendList(

    @SerializedName("data")
    val `data`: List<FriendListData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)
data class FriendListData(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)

