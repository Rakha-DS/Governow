package com.cpastone.governow.home.ui.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.cpastone.governow.customview.EmailEditText
import com.cpastone.governow.customview.PasswordEditText
import com.capstone.governow.data.model.UserModel
import com.capstone.governow.databinding.ActivityLoginScreenBinding
import com.cpastone.governow.home.HomeActivity
import com.cpastone.governow.home.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var emailEdit: EmailEditText
    private lateinit var passwordEdit: PasswordEditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking(Dispatchers.IO)  {
            val user = viewModel.getSession()


            if(user.token != null && user.token != ""){
                startActivity(Intent(this@LoginScreenActivity, HomeActivity::class.java))
                finish()
            }


            binding = ActivityLoginScreenBinding.inflate(layoutInflater)
            setContentView(binding.root)

            emailEdit = binding.emailEditTextLayout
            passwordEdit = binding.passwordEditTextLayout
            buttonSubmit = binding.loginButton

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
        buttonSubmit.setOnClickListener {
            buttonSubmit.isEnabled = false
            val emailLogin = emailEdit.emailVal
            val passwordLogin = passwordEdit.passwordVal

            lifecycleScope.launch {
                try {
                    val login = viewModel.loginUser(emailLogin, passwordLogin)
                    val token = login?.token

                    if (login != null && token != null) {
                        val user = viewModel.getProfile(token)?.data
                        val fullName = user?.fullName
                        val username = user?.username
                        val email = user?.email

                        if (fullName != null && username != null && email != null) {
                            viewModel.saveSession(UserModel(fullName, email, username, token))
                            startActivity(Intent(this@LoginScreenActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            showErrorDialog()
                        }
                    } else {
                        showErrorDialog()
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", e.message.toString())
                    showErrorDialog()
                } finally {
                    buttonSubmit.isEnabled = true
                }
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Sorry!")
            setMessage("Login failed. Please check your email and password and try again.")
            setPositiveButton("Try Again") { _, _ -> }
            create()
            show()
        }
    }
}
