package com.cpastone.governow.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cpastone.governow.api.ApiConfig
import com.capstone.governow.data.model.UserModel
import com.capstone.governow.data.repository.UserRepository
import com.capstone.governow.data.respone.ProfileData
import com.capstone.governow.data.respone.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
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

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

}
