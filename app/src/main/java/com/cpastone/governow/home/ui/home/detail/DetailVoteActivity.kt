package com.cpastone.governow.home.ui.home.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.capstone.governow.R
import com.capstone.governow.databinding.ActivityDetailVoteBinding
import com.cpastone.governow.data.model.PollItem

class DetailVoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailVoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val voteQuestion = intent.getStringExtra("VOTE_QUESTION")
        val pollItems = intent.get("POLL_ITEMS") as ArrayList<PollItem>

        binding.tvVoteQuestion.text = voteQuestion

        pollItems.forEach { pollItem ->
            val textView = TextView(this).apply {
                text = "${pollItem.option}: ${pollItem.voteCount} votes"
                textSize = 16f
            }
            binding.llVoteOptions.addView(textView)
        }
    }
}
