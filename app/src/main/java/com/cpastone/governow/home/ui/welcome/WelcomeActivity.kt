package com.cpastone.governow.home.ui.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.governow.databinding.ActivityLoginScreenBinding
import com.capstone.governow.databinding.ActivityWelcomeBinding
import com.cpastone.governow.home.HomeActivity
import com.cpastone.governow.home.ViewModelFactory
import com.cpastone.governow.home.ui.login.LoginScreenActivity
import com.cpastone.governow.home.ui.login.LoginScreenViewModel
import com.cpastone.governow.home.ui.signup.SignUpScreenActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class WelcomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking(Dispatchers.IO)  {
            val user = viewModel.getSession()

            Log.d("hihi", user.token.toString())

            if(user.token != null && user.token != ""){
                startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java))
                finish()
            }

            binding = ActivityWelcomeBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupView()
            setupAction()
        }


    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginScreenActivity::class.java))
        }


        binding.signupButton2.setOnClickListener {
            startActivity(Intent(this, SignUpScreenActivity::class.java))
        }
    }
}