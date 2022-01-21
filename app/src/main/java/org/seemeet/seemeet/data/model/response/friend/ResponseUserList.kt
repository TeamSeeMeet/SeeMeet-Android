package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

data class ResponseUserList(
    @SerializedName("data")
    val `data`: UserListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class UserListData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("idFirebase")
    val idFirebase: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("isNoticed")
    val isNoticed: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)