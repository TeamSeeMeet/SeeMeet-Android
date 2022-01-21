package org.seemeet.seemeet.data.model.response.invitationResponse
import com.google.gson.annotations.SerializedName


data class ResponseYesInvitationResponse(
    @SerializedName("data")
    val `data`: List<YesInvitationResponseData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class YesInvitationResponseData(
    @SerializedName("invitationDate")
    val invitationDate: YesInvitationDate,
    @SerializedName("responseId")
    val responseId: Int
)

data class YesInvitationDate(
    @SerializedName("date")
    val date: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitation_id")
    val invitationId: Int,
    @SerializedName("start")
    val start: String
)
