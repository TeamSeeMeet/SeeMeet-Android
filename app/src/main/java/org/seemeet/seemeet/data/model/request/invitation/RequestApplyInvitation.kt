package org.seemeet.seemeet.data.model.request.invitation

import java.util.*

//초대장 신청
data class RequestApplyInvitation(
    val gusts : String,
    val invitationTitle : String,
    val invitationDesc : String,
    val date : Date,
    val start : String,
    val end : String
)
