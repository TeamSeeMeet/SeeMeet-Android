package org.seemeet.seemeet.data.model.response.invitation

import java.util.*

data class ResponseApplyInvitation(
    val status : Int,
    val success : Boolean,
    val message : String,
    val data : Data
)
data class Data(
    val id : Int,
    val hostId : String,
    val guests : List<UserData>,
    val invitationDates : List<InvitationDateData>,
    val createdAt : Date,
    val isConfirmed : Boolean,
    val isCancled: Boolean,
    val isDeleted : Boolean
)
data class UserData(
    val userId : Int,
    val userName : String
)
data class InvitationDateData(
    val invitationDatedId : Int,
    val invitationId : Int,
    val date : String,
    val start : String,
    val end : String
)
