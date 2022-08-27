package org.seemeet.seemeet.data.model.response.login


import com.google.gson.annotations.SerializedName

data class ResponseKakaoLogin(
    val data: SocialData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class SocialData(
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String,
    @SerializedName("user")
    val user: ExUser
)

data class ExUser(
    //@SerializedName("created_at")
    val createdAt: String,
    val email: String? = null,
    val id: Int,
    val idFirebase: String? = null,
    //@SerializedName("img_link")
    val imgLink:  String? = null,
    //@SerializedName("img_name")
    val imgName: String? = null,
    val isDeleted: Boolean,
    val isNoticed: Boolean,
    val nickname: String? = null,
    val provider: String,
    val push: Boolean,
    val password: String,
    val socialId: String,
    val updatedAt: String,
    val username: String
)