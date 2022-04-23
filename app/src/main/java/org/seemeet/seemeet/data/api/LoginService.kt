package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.request.login.RequestKakaoLogin
import org.seemeet.seemeet.data.model.request.login.RequestLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseKakaoLogin
import org.seemeet.seemeet.data.model.response.login.ResponseLoginList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Content-Type:application/json")
    @POST("auth/login")
    suspend fun postLogin(
        @Body body : RequestLoginList
    ): ResponseLoginList

    @POST("auth/login")
    suspend fun postKakaoLogin(
        @Body body : RequestKakaoLogin
    ): ResponseKakaoLogin
}