package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

data class ResponseAddFriendData(
    @SerializedName("data")
    val `data`: AddFriendData,
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
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("receiver")
    val `receiver`: Int,
    @SerializedName("receiverDeleted")
    val receiverDeleted: Boolean,
    @SerializedName("sender")
    val sender: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)