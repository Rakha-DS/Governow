package com.cpastone.governow.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityHomeBinding
import com.cpastone.governow.home.ui.aspiration.AddAspirationActivity
import com.cpastone.governow.home.ui.home.HomeViewModel
import com.cpastone.governow.home.ui.login.LoginScreenActivity

class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!auth()){
            startActivity(Intent(this@HomeActivity, LoginScreenActivity::class.java))
            finish()
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Set content view using binding.root
        setSupportActionBar(binding.ToolbarMain)
//        supportActionBar?.hide()
        setupNavigation()

        binding.buttonadd.setOnClickListener {
            val intent = Intent(this, AddAspirationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun auth(): Boolean
    {
        val user = viewModel.getSession()

        Log.d("hohot", user.token.toString());

        return (user.token != "")
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_news,
                R.id.navigation_form,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}
