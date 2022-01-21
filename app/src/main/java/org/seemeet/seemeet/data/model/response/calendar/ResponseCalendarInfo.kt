package org.seemeet.seemeet.data.model.response.calendar

data class ResponseCalendarInfo(
    val status: Int,
    val success: Boolean,
    val data: List<CalendarEvent>
)
data class CalendarEvent(
    val planId: Int,
    val invitationTitle: String,
    val date: String,
    val start: String,
    val end: String,
    val users: List<UserData>,
)

data class UserData(
    val user_id: Int,
    val username: String
)