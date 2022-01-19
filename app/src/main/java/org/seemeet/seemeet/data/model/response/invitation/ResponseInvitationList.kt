package org.seemeet.seemeet.data.model.response.invitation
import com.google.gson.annotations.SerializedName

data class ResponseInvitationList(
    @SerializedName("data")
    val `data`: InvitationListData,
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
    @SerializedName("is_cancled")
    val isCancled: Boolean,
    @SerializedName("is_confirmed")
    val isConfirmed: Boolean,
    @SerializedName("planId")
    val planId: Int
)

data class Invitation(
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
    @SerializedName("is_cancled")
    val isCancled: Boolean,
    @SerializedName("is_confirmed")
    val isConfirmed: Boolean,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("isReceived")
    val isReceived: Boolean
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