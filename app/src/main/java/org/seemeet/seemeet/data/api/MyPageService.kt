package org.seemeet.seemeet.data.api

import okhttp3.MultipartBody
import org.seemeet.seemeet.data.model.request.mypage.RequestChangePW
import org.seemeet.seemeet.data.model.request.mypage.RequestChangePush
import org.seemeet.seemeet.data.model.response.mypage.ResponseChangePW
import org.seemeet.seemeet.data.model.response.mypage.ResponseChangePush
import org.seemeet.seemeet.data.model.response.mypage.ResponseMyPageProfile
import retrofit2.Call
import retrofit2.http.*

interface MyPageService {
    @Multipart
    @POST("user/upload")
    suspend fun postProfile(
        @Header("accesstoken") token: String,
        @Part file: MultipartBody.Part
    ): ResponseMyPageProfile

    @PUT("user/password")
    suspend fun putChangePW(
        @Header("accesstoken") token: String,
        @Body body: RequestChangePW
    ): ResponseChangePW

    @POST("user/push")
    suspend fun postChangePush(
        @Header("accesstoken") token: String,
        @Body body: RequestChangePush
    ): ResponseChangePush
}