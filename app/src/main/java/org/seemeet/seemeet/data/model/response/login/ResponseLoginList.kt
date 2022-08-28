package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val user: User,
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
)

data class User(
    val id: Int,
    val email: String? = null,
    @SerializedName("id_firebase")
    val idFirebase: String? = null,
    val username: String,
    @SerializedName("is_noticed")
    val isNoticed: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    val provider: String,
    @SerializedName("social_id")
    val socialId: String,
    val nickname: String? = null,
    @SerializedName("img_link")
    val imgLink: String? = null,
    val push: Boolean,
    val password: String,
    @SerializedName("img_name")
    val imgName: String? = null,
    val fcm: String?
)