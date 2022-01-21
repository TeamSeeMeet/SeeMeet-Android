package org.seemeet.seemeet.data.model.request.invitation

import org.seemeet.seemeet.data.local.ApplyFriendData
import java.util.*
import kotlin.collections.ArrayList

//초대장 신청
data class RequestApplyInvitation(
    val guests : List<ApplyFriendData>,
    val invitationTitle : String,
    val invitationDesc : String,
    val date : List<String>,
    val start : List<String>,
    val end : List<String>
)
