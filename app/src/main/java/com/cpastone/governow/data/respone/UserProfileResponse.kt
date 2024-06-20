package com.cpastone.governow.data.respone

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ProfileData
)

data class ProfileData(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("profile_image") val profileImage: String
)