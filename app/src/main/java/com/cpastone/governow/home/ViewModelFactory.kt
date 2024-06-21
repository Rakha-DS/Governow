package com.cpastone.governow.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.governow.data.repository.UserRepository
import com.capstone.governow.di.Injection
import com.cpastone.governow.home.ui.aspiration.AddAspirationViewModel
import com.cpastone.governow.home.ui.edit.EditProfileViewModel
import com.cpastone.governow.home.ui.home.HomeViewModel
import com.cpastone.governow.home.ui.home.detail.DetailVoteViewModel
import com.cpastone.governow.home.ui.login.LoginScreenViewModel
import com.cpastone.governow.home.ui.profile.ProfileViewModel
import com.cpastone.governow.home.ui.signup.SignUpScreenViewModel
import com.cpastone.governow.home.ui.welcome.WelcomeViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> {
                LoginScreenViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpScreenViewModel::class.java) -> {
                SignUpScreenViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddAspirationViewModel::class.java) -> {
                AddAspirationViewModel() as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailVoteViewModel::class.java) -> {
                DetailVoteViewModel() as T
            }
            else -> throw IllegalArgumentException("unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}