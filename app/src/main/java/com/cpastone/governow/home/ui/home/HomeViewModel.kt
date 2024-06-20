package com.cpastone.governow.home.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.governow.data.model.UserModel
import com.capstone.governow.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
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