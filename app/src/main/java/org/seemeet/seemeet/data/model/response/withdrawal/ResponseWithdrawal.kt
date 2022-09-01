package org.seemeet.seemeet.data.model.response.withdrawal

<<<<<<< HEAD
import org.seemeet.seemeet.data.model.response.login.ExUser
=======
import org.seemeet.seemeet.data.model.response.register.UserInfo
>>>>>>> develop

data class ResponseWithdrawal(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
<<<<<<< HEAD
    val user: ExUser,
    val accesstoken: String
=======
    val user: UserInfo
>>>>>>> develop
)