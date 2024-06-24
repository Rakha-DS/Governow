package com.cpastone.governow.home.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cpastone.governow.api.ApiConfig
import com.cpastone.governow.data.request.RegisterRequest
import com.cpastone.governow.data.respone.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class SignUpScreenViewModel():ViewModel() {
    fun signUpUser(registerRequest: RegisterRequest): RegisterResponse? {
        var result: RegisterResponse? = null

        runBlocking(Dispatchers.IO) {
            val call = ApiConfig.apiInstance.signUpUser(registerRequest)
            val response = call.execute()
            if (response.isSuccessful) {
                result = RegisterResponse(response.body()?.message)
            }
        }

        return result
    }
}