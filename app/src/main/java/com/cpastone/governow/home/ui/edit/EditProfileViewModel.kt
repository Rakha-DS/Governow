package com.cpastone.governow.home.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cpastone.governow.api.ApiConfig
import com.capstone.governow.data.model.UserModel
import com.capstone.governow.data.repository.UserRepository
import com.capstone.governow.data.respone.ProfileData
import com.capstone.governow.data.respone.ProfileResponse
import com.cpastone.governow.data.request.UpdateProfileRequest
import com.cpastone.governow.data.respone.UpdateProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.IOException

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getProfile(token: String): ProfileResponse? {
        var result: ProfileResponse? = null

        runBlocking(Dispatchers.IO) {
            val call = ApiConfig.apiInstance.getProfile("Bearer $token")
            val response = call.execute()
            if (response.isSuccessful) {
                result = ProfileResponse(response.body()?.message.toString(), ProfileData(response.body()?.data?.fullName.toString(), response.body()?.data?.email.toString(), response.body()?.data?.username.toString(), response.body()?.data?.profile_image.toString()))
            }
        }

        return result
    }

    fun updateProfile(token: String, updateProfileRequest: UpdateProfileRequest): UpdateProfileResponse? {
        var result: UpdateProfileResponse? = null

        runBlocking(Dispatchers.IO) {
            try {
                val call = ApiConfig.apiInstance.updateProfile(
                    "Bearer $token",
                    updateProfileRequest.fullName,
                    updateProfileRequest.email,
                    updateProfileRequest.username,
                    updateProfileRequest.attachment
                )
                val response = call.execute()
                Log.d("updateProfile", response.toString())
                if (response.isSuccessful) {
                    result = UpdateProfileResponse(response.body()?.message.toString())
                } else {
                    Log.e("updateProfile", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e("updateProfile", "Network error: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("updateProfile", "Unexpected error: ${e.message}", e)
            }
        }

        return result
    }


    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}