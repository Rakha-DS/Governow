package com.cpastone.governow.api

import com.capstone.governow.data.request.LoginRequest
import com.cpastone.governow.data.request.RegisterRequest
import com.capstone.governow.data.respone.LoginResponse
import com.capstone.governow.data.respone.ProfileResponse
import com.cpastone.governow.data.request.AspirationRequest
import com.cpastone.governow.data.respone.AspirationResponse
import com.cpastone.governow.data.respone.GetAllAspirationResponse
import com.cpastone.governow.data.respone.NewsResponse
import com.cpastone.governow.data.respone.PostResponse
import com.cpastone.governow.data.respone.RegisterResponse
import com.cpastone.governow.data.respone.UpdateProfileResponse
import com.cpastone.governow.data.respone.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {

    @POST("user/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("user/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @Multipart
    @PUT("user/profile")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part("fullName") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("username") username: RequestBody,
        @Part attachment: MultipartBody.Part?
    ): Call<UpdateProfileResponse>

    @POST("user/register")
    fun signUpUser(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>

    @GET("user/profile")
    fun getUserProfile(): Call<UserProfileResponse>

    @GET("news/all")
    fun getAllNews(): Call<NewsResponse>

    @GET("post/all")
    fun getAllPosts(): Call<PostResponse>

    @Multipart
    @POST("aspirations")
    fun createAspiration(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("date") date: RequestBody,
        @Part("location") location: RequestBody,
        @Part attachment: MultipartBody.Part?
    ): Call<AspirationResponse>

    @GET("aspirations/all")
    fun getAllAspirations(): Call<GetAllAspirationResponse>
}
