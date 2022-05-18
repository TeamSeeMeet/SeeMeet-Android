package org.seemeet.seemeet.data.model.response.register


import com.google.gson.annotations.SerializedName
import org.seemeet.seemeet.data.model.response.login.ExUser

data class ResponseRegisterNameId(
    val data: ExUser,
    val status: Int,
    val success: Boolean
)