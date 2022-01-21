package org.seemeet.seemeet.data.model.response.invitation
import com.google.gson.annotations.SerializedName


data class ResponseReceiveInvitation(
    @SerializedName("data")
    val `data`: ReceiveInvitationData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class ReceiveInvitationData(
    @SerializedName("invitation")
    val invitation: ReceiveInvitation,
    @SerializedName("invitationDates")
    val invitationDates: List<ReceiveInvitationDate>,
    @SerializedName("isResponse")
    val isResponse: Boolean,
    @SerializedName("newGuests")
    val newGuests: List<ReceiveGuest>
)

data class ReceiveInvitation(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("host")
    val host: ReceiveHost,
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

data class ReceiveInvitationDate(
    @SerializedName("date")
    val date: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitationId")
    val invitationId: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean,
    @SerializedName("start")
    val start: String
)

data class ReceiveGuest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)

data class ReceiveHost(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)