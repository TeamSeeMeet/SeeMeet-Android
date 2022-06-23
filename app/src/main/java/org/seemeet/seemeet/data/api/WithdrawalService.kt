package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.response.withdrawal.ResponseWithdrawal
import retrofit2.http.Header
import retrofit2.http.PUT

interface WithdrawalService {
    @PUT("auth/withdrawal")
    suspend fun putWithdrawal(
        @Header("accesstoken") token: String
    ): ResponseWithdrawal
}