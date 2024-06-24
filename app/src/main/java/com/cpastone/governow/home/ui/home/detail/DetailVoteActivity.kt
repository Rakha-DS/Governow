package com.cpastone.governow.home.ui.home.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityDetailVoteBinding
import com.cpastone.governow.data.model.PollItem
import com.cpastone.governow.data.model.PostItem
import com.cpastone.governow.data.request.AspirationRequest
import com.cpastone.governow.home.ui.aspiration.AddAspirationViewModel

class DetailVoteActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailVoteViewModel> {
        com.cpastone.governow.home.ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra("POST_ID")

        val post = fetchPost(postId.toString())

        binding.tvVoteQuestion.text = post?.caption

        post?.polls?.forEach { pollItem ->
            val textView = TextView(this).apply {
                text = "${pollItem.option}: ${pollItem.voteCount} votes"
                textSize = 16f
            }
            binding.llVoteOptions.addView(textView)
        }
    }

    private fun fetchPost(id: String): PostItem?
    {
        val response = viewModel.getPost(id)

        if(response != null && response.message != ""){
            return response.data
        }
        return null
    }


}
