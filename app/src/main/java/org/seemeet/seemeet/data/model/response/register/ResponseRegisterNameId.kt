package org.seemeet.seemeet.data.model.response.register


data class ResponseRegisterNameId(
    val data: ExUser,
    val status: Int,
    val success: Boolean
)

data class ExUser(
    val createdAt: String,
    val email: String? = null,
    val id: Int,
    val idFirebase: String? = null,
    val isDeleted: Boolean,
    val isNoticed: Boolean,
    val nickname: String? = null,
    val provider: String,
    val socialId: String,
    val updatedAt: String,
    val username: String
)