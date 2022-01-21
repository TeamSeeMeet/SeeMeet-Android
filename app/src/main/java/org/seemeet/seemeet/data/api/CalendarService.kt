package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.response.calendar.ResponseCalendarInfo
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarService {
    @GET("plan/month/{year}/{month}")
    suspend fun getFriendList(
        @Header("accesstoken") token : String,
        @Path("year") year : String,
        @Path("month") month : String,
    ): ResponseCalendarInfo
}