package org.seemeet.seemeet.data.model.request.register

import com.google.gson.annotations.SerializedName

data class RequestRegisterNameId(
    val name: String,
    @SerializedName("nickname")
    val nickName: String
)
