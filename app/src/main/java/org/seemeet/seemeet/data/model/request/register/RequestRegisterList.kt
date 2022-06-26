package org.seemeet.seemeet.data.model.request.register

data class RequestRegisterList(
    val email: String,
    val password: String,
    val passwordConfirm: String
)