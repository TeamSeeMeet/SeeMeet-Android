package org.seemeet.seemeet.data.api

import okhttp3.MultipartBody
import org.seemeet.seemeet.data.model.response.mypage.ResponseMyPageProfile
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyPageService {
    @Multipart
    @POST("user/upload")
    fun postProfile(
        @Header("accesstoken") token: String,
        @Part file: MultipartBody.Part
    ): Call<ResponseMyPageProfile>
}