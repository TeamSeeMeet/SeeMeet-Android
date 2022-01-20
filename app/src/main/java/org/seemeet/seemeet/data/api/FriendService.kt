package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.request.friend.RequestAddFriendData
import org.seemeet.seemeet.data.model.request.friend.RequestUserData
import org.seemeet.seemeet.data.model.response.friend.ResponseAddFriendData
import org.seemeet.seemeet.data.model.response.friend.ResponseFriendList
import org.seemeet.seemeet.data.model.response.friend.ResponseUserList
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FriendService {

    @GET("friend/list")
    suspend fun getFriendList(
       @Header("accesstoken") token : String
    ): ResponseFriendList

    @POST("friend/search")
    suspend fun searchUserList(
        @Header("accesstoken") token : String,
        @Body body: RequestUserData
    ): ResponseUserList

    @POST("friend/addFriend")
    suspend fun addFriendData(
        @Header("accesstoken") token : String,
        @Body body: RequestAddFriendData
    ) : ResponseAddFriendData
}