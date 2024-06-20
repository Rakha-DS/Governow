package com.cpastone.governow.data.request

data class RegisterRequest(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)

