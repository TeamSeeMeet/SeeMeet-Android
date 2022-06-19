package org.seemeet.seemeet.data.model.response.invitation
import com.google.gson.annotations.SerializedName

data class ResponseInvitationList(
    @SerializedName("data")
    val `data`: InvitationListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class InvitationListData(
    @SerializedName("confirmedAndCanceld")
    val confirmedAndCanceld: List<ConfirmedAndCanceld>,
    @SerializedName("invitations")
    val invitations: List<Invitation>
)

data class ConfirmedAndCanceld(
    @SerializedName("guests")
    val guests: List<Guest>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitation_title")
    val invitationTitle: String,
    @SerializedName("is_canceled")
    val isCanceled: Boolean,
    @SerializedName("is_confirmed")
    val isConfirmed: Boolean,
    @SerializedName("planId")
    val planId: Int,
    @SerializedName("days")
    val days: Int
)

data class Invitation(
    @SerializedName("canceled_at")
    val canceledAt: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("guests")
    val guests: List<GuestX>,
    @SerializedName("host_id")
    val hostId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invitation_desc")
    val invitationDesc: String,
    @SerializedName("invitation_title")
    val invitationTitle: String,
    @SerializedName("is_canceled")
    val isCanceled: Boolean,
    @SerializedName("is_confirmed")
    val isConfirmed: Boolean,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("isReceived")
    val isReceived: Boolean,
    @SerializedName("isResponse")
    val isResponse: Boolean
)

data class Guest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("impossible")
    val impossible: Boolean,
    @SerializedName("isResponse")
    val isResponse: Boolean,
    @SerializedName("username")
    val username: String
)

data class GuestX(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isResponse")
    val isResponse: Boolean,
    @SerializedName("username")
    val username: String
)