package com.cpastone.governow.home.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpastone.governow.api.ApiConfig
import com.capstone.governow.data.repository.UserRepository
import com.capstone.governow.data.request.LoginRequest
import com.capstone.governow.data.respone.ProfileData
import com.capstone.governow.data.respone.LoginResponse
import com.capstone.governow.data.respone.ProfileResponse
import com.capstone.governow.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginScreenViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): UserModel
    {
        var result = UserModel("", "", "", "")
        viewModelScope.launch {
            repository.getSession().collect { userModel ->
                result = userModel
            }
        }

        return result
    }

    fun loginUser(email: String, password: String): LoginResponse? {
        var result: LoginResponse? = null
        Log.d("email", email)
        Log.d("password", password)


        runBlocking(Dispatchers.IO) {
            val call = ApiConfig.apiInstance.loginUser(LoginRequest(email, password))
            val response = call.execute()
            Log.d("hohox", response.toString())
            Log.d("hohoy", response.body()?.message.toString())
            if (response.isSuccessful) {
                result = LoginResponse(response.body()?.message, response.body()?.token)
            }else{
                Log.d("hoho", "denis")
            }
        }

        return result
    }

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

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}