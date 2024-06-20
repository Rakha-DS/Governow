package com.cpastone.governow.home.ui.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cpastone.governow.customview.EmailEditText
import com.cpastone.governow.customview.PasswordEditText
import com.cpastone.governow.data.request.RegisterRequest
import com.capstone.governow.databinding.ActivitySignUpScreenBinding
import com.cpastone.governow.home.ViewModelFactory
import com.cpastone.governow.home.ui.login.LoginScreenActivity
import kotlinx.coroutines.launch

class SignUpScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var fullNameEdit: EditText
    private lateinit var usernameEdit: EditText
    private lateinit var emailEdit: EmailEditText
    private lateinit var passwordEdit: PasswordEditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullNameEdit = binding.edRegisterName
        usernameEdit = binding.edRegisterUsername
        emailEdit = binding.emailEditTextLayout
        passwordEdit = binding.passwordEditTextLayout
        buttonSubmit = binding.buttonRegis

        setupView()
        setupAction()
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
            val fullName = fullNameEdit.text.toString()
            val username = usernameEdit.text.toString()
            val email = emailEdit.emailVal
            val password = passwordEdit.passwordVal

            lifecycleScope.launch {
                try {
                    val response = viewModel.signUpUser(RegisterRequest(fullName,  username , email, password))
                    val message = response?.message

                    if (response != null && message != null) {
                        startActivity(Intent(this@SignUpScreenActivity, LoginScreenActivity::class.java))
                        finish()
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
            setMessage("Register failed. Please check all field and try again.")
            setPositiveButton("Try Again") { _, _ -> }
            create()
            show()
        }
    }
}