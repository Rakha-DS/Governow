package com.cpastone.governow.home.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.governow.data.repository.UserRepository
import com.capstone.governow.data.model.UserModel
import kotlinx.coroutines.launch

class WelcomeViewModel(private val repository: UserRepository) : ViewModel() {
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


}