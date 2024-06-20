package com.cpastone.governow.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class AspirationRequest(
    val title: RequestBody,
    val description: RequestBody,
    val date: RequestBody,
    val location: RequestBody,
    val attachment: MultipartBody.Part?
)
