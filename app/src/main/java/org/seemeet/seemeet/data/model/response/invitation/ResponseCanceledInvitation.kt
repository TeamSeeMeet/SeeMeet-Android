package org.seemeet.seemeet.data.model.response.invitation
import com.google.gson.annotations.SerializedName


data class ResponseCanceledInvitation(
    @SerializedName("data")
    val `data`: CanceledData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class CanceledData(
    @SerializedName("canceledAt")
    val canceledAt: Any,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("guest")
    val guest: List<CanceledGuest>,
    @SerializedName("hostId")
    val hostId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitationDesc")
    val invitationDesc: String,
    @SerializedName("invitationTitle")
    val invitationTitle: String,
    @SerializedName("isCanceled")
    val isCanceled: Boolean,
    @SerializedName("isConfirmed")
    val isConfirmed: Boolean,
    @SerializedName("isDeleted")
    val isDeleted: Boolean
)

data class CanceledGuest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)