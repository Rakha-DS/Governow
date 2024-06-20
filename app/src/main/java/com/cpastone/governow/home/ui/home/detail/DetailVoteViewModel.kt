package com.cpastone.governow.home.ui.home.detail

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
import com.cpastone.governow.data.respone.DetailPostResponse
import com.cpastone.governow.data.respone.UpdateProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.IOException

class DetailVoteViewModel() : ViewModel() {
    fun getPost(id: String): DetailPostResponse? {
        var result: DetailPostResponse? = null

        runBlocking(Dispatchers.IO) {
            val call = ApiConfig.apiInstance.getPost(id)
            val response = call.execute()
            if (response.isSuccessful) {
                result = DetailPostResponse(response.body()?.message.toString(), response.body()?.data)
            }
        }

        return result
    }
}