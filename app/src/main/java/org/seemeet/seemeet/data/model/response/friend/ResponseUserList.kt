package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

//유저 목록 요청
data class ResponseUserList(

    @SerializedName("data")
    val `data`: List<UserListData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class UserListData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("idFirebase")
    val idFirebase: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("birth")
    val birth: String,
    @SerializedName("isNoticed")
    val isNoticed: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
)
