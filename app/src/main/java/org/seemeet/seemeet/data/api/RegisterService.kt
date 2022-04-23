package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.request.register.RequestRegisterList
import org.seemeet.seemeet.data.model.request.register.RequestRegisterNameId
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterList
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterNameId
import retrofit2.http.*

interface RegisterService {
    @Headers("Content-Type:application/json")
    @POST("auth/register")
    suspend fun postRegister(
        @Body body: RequestRegisterList
    ): ResponseRegisterList

    @PUT("auth/register")
    suspend fun postRegisterNameId(
        @Header("accesstoken") token: String,
        @Body body: RequestRegisterNameId
    ): ResponseRegisterNameId
}