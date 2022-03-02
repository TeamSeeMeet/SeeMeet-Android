package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.request.register.RequestRegisterList
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterList
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterService {
    @Headers("Content-Type:application/json")
    @POST("auth/register")
    suspend fun postRegister(
        @Body body: RequestRegisterList
    ): ResponseRegisterList
}