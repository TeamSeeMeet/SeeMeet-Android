package org.seemeet.seemeet.data.model.request.invitationResponse
import com.google.gson.annotations.SerializedName


data class RequestInvitationResponse(
    @SerializedName("invitationDateIds")
    val invitationDateIds: List<Int>
)
