package org.seemeet.seemeet.data.model.response.invitation
import com.google.gson.annotations.SerializedName

//특정 요청 조회

data class ResponseSendInvitation(
    @SerializedName("data")
    val `data`: SendInvitationData,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class SendInvitationData(
    @SerializedName("invitation")
    val invitation: SendInvitation,
    @SerializedName("invitationDates")
    val invitationDates: List<InvitationDate>
)

data class SendInvitation(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("guests")
    val guests: List<SendGuest>,
    @SerializedName("host")
    val host: SendHost,
    @SerializedName("host_id")
    val hostId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitation_desc")
    val invitationDesc: String,
    @SerializedName("invitation_title")
    val invitationTitle: String,
    @SerializedName("is_cancled")
    val isCancled: Boolean,
    @SerializedName("is_confirmed")
    val isConfirmed: Boolean,
    @SerializedName("is_deleted")
    val isDeleted: Boolean
)

data class InvitationDate(
    @SerializedName("date")
    val date: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitation_id")
    val invitationId: Int,
    @SerializedName("respondent")
    val respondent: List<Any>,
    @SerializedName("start")
    val start: String
)

data class SendGuest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isResponse")
    val isResponse: Boolean,
    @SerializedName("username")
    val username: String
)

data class SendHost(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)