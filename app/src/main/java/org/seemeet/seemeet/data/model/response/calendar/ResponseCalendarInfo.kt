package org.seemeet.seemeet.data.model.response.calendar

data class ResponseCalendarInfo(
    val status: Int,
    val success: Boolean,
    val data: List<CalendarEvent>
)

data class CalendarEvent(
    val planId: Int = 0,
    val invitationTitle: String = "",
    val date: String = "",
    val start: String = "",
    val end: String = "",
    val users: List<UserData> = listOf()
)

data class UserData(
    val user_id: Int = 0,
    val username: String = ""
)