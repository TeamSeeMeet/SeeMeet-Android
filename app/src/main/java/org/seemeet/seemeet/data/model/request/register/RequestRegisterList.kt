package org.seemeet.seemeet.data.model.request.register

import java.util.*

data class RequestRegisterList(
    val username : String,
    val email : String,
    val password : String,
    val passwordConfirm : String
)
