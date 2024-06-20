package com.capstone.governow.data.respone

data class ProfileResponse(
    val message: String,
    val data: ProfileData
)

data class ProfileData(
    val fullName: String,
    val email: String,
    val username: String,
    val profile_image : String?
)

data class Profile(
    val username: String,
    val profile_image : String?
)
