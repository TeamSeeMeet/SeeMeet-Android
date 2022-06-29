package org.seemeet.seemeet.data.model.response.mypage

import com.google.gson.annotations.SerializedName

data class ResponseChangePush(
    @SerializedName("data")
    val `data`: MypageData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class MypageData(
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
    val username: String,
    @SerializedName("imgLink")
    val imgLink: String?,
    @SerializedName("push")
    val push: Boolean,
    @SerializedName("password")
    val password: String,
    @SerializedName("imgName")
    val imgName: String,
    @SerializedName("fcm")
    val fcm: String,
)

