package com.cpastone.governow.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateProfileRequest(
    val fullName: RequestBody,
    val email: RequestBody,
    val username: RequestBody,
    val attachment: MultipartBody.Part?
)

