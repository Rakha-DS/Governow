package com.cpastone.governow.home.ui.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityDetailFormBinding

class DetailFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the extras from the intent
        val title = intent.getStringExtra("FORM_ITEM_TITLE")
        val description = intent.getStringExtra("FORM_ITEM_DESCRIPTION")
        val date = intent.getStringExtra("FORM_ITEM_DATE")
        val location = intent.getStringExtra("FORM_ITEM_LOCATION")
        val imageUrl = intent.getStringExtra("FORM_ITEM_IMAGE_RES_ID")

        // Set the retrieved values to the appropriate UI elements
        binding.storyTitle.text = title
        binding.tvDescription.text = description
        binding.tvDate.text = date
        binding.tvLocation.text = location
        Glide.with(binding.ivDetailPhoto.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(binding.ivDetailPhoto)
    }
}