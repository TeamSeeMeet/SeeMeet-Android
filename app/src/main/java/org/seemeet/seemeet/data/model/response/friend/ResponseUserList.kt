package org.seemeet.seemeet.data.model.response.friend

import com.google.gson.annotations.SerializedName

data class ResponseUserList(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class Data(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("idFirebase")
    val idFirebase: Any,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("isNoticed")
    val isNoticed: Boolean,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("socialId")
    val socialId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)