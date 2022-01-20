package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList

import retrofit2.http.GET
import retrofit2.http.Header

interface FriendService {

    @GET("friend/list")
    suspend fun getFriendList(
       @Header("accesstoken") token : String
    ): ResponseFriendList
}