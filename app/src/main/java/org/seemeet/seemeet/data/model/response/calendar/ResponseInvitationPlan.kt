package org.seemeet.seemeet.data.model.response.calendar

data class ResponseInvitationPlan(
    val status: Int,
    val success: Boolean,
    val data: List<InvitationPlan>
)

data class InvitationPlan(
    val planId: Int,
    val invitationTitle: String,
    val date: String,
    val start: String,
    val end: String,
)