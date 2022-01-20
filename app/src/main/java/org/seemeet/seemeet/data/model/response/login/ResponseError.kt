package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("errorMessage") val errorMessage: String
)
