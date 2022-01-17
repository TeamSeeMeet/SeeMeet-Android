package org.seemeet.seemeet.ui.main.calendar

import java.time.LocalDate

data class CalendarEvent(
    val planId: Int,
    val planTitle: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val users: List<UserData>,
)

data class UserData(
    val userId: Int,
    val userName: String
)


