package com.cpastone.governow.home.ui.news


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityDetailNewsBinding

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val imageUrl = intent.getStringExtra("imageUrl")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")

        binding.storyTitle.text = title
        binding.textDescription.text = date
        binding.tvDetailDescription.text = description
        Glide.with(this).load(imageUrl).into(binding.ivDetailPhoto)
    }
}