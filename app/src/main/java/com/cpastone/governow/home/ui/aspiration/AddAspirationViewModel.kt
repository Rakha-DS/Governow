package com.cpastone.governow.home.ui.aspiration

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cpastone.governow.api.ApiConfig
import com.cpastone.governow.data.request.AspirationRequest
import com.cpastone.governow.data.respone.AspirationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.IOException

class AddAspirationViewModel() : ViewModel() {
    fun createAspiration(createAspirationRequest: AspirationRequest): AspirationResponse? {
        var result: AspirationResponse? = null

        runBlocking(Dispatchers.IO) {
            try {
                val call = ApiConfig.apiInstance.createAspiration(createAspirationRequest.title, createAspirationRequest.description, createAspirationRequest.date, createAspirationRequest.location, createAspirationRequest.attachment)
                val response = call.execute()
                Log.d("createAspiration", response.toString())
                if (response.isSuccessful) {
                    result = AspirationResponse(response.body()?.message.toString())
                } else {
                    Log.e("createAspiration", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e("createAspiration", "Network error: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("createAspiration", "Unexpected error: ${e.message}", e)
            }
        }

        return result
    }
}