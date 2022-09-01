package org.seemeet.seemeet.data.model.response.login

import com.google.gson.annotations.SerializedName
<<<<<<< HEAD
=======
import org.seemeet.seemeet.data.model.response.register.UserInfo
>>>>>>> develop

data class ResponseLoginList(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
<<<<<<< HEAD
    val user: ExUser,
=======
    val user: UserInfo,
>>>>>>> develop
    @SerializedName("accesstoken")
    val accessToken: String,
    @SerializedName("refreshtoken")
    val refreshToken: String
<<<<<<< HEAD
)
=======
)
>>>>>>> develop
