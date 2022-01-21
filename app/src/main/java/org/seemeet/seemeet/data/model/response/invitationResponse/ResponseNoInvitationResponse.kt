package org.seemeet.seemeet.data.model.response.invitationResponse
import com.google.gson.annotations.SerializedName


data class ResponseNoInvitationResponse(
    @SerializedName("data")
    val `data`: NoInvitationResponseData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class NoInvitationResponseData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("impossible")
    val impossible: Boolean,
    @SerializedName("invitationId")
    val invitationId: Int
)