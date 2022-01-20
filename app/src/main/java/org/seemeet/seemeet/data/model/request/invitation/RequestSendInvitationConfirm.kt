package org.seemeet.seemeet.data.model.request.invitation


import com.google.gson.annotations.SerializedName

data class RequestSendInvitationConfirm(
    @SerializedName("dateId")
    val dateId: Int
)