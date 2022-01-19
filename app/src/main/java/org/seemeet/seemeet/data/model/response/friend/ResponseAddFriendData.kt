package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

data class ResponseAddFriendData(

    @SerializedName("data")
    val `data`: List<AddFriendData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)
data class AddFriendData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sender")
    val sender: Int,
    @SerializedName("receiver")
    val receiver: Int,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String
)

