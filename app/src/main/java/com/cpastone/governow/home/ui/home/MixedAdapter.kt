package com.cpastone.governow.home.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.capstone.governow.R
import com.capstone.governow.databinding.ItemListMainBinding
import com.capstone.governow.databinding.ItemListMainPollingBinding
import com.cpastone.governow.data.model.PollItem
import com.cpastone.governow.data.model.PostItem
import com.cpastone.governow.home.ui.home.detail.DetailVoteActivity

class MixedAdapter(private val itemList: List<PostItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_POLL = 0
        private const val VIEW_TYPE_TEXT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_POLL -> {
                val binding = ItemListMainPollingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PollViewHolder(binding)
            }
            VIEW_TYPE_TEXT -> {
                val binding = ItemListMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TextViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is PollViewHolder -> holder.bind(item)
            is TextViewHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return if (item.polls != null) {
            VIEW_TYPE_POLL
        } else {
            VIEW_TYPE_TEXT
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class PollViewHolder(private val binding: ItemListMainPollingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            binding.tvPollTitle.text = item.caption
            binding.tvPollDescription.text = item.userId
            binding.radioGroup.removeAllViews()

            item.polls?.forEach { pollItem ->
                val radioButton = RadioButton(binding.radioGroup.context).apply {
                    text = "${pollItem.option}: ${pollItem.voteCount} votes"
                }
                binding.radioGroup.addView(radioButton)
            }

            binding.btnViewResults.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailVoteActivity::class.java).apply {
                    putExtra("POST_ID", item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    inner class TextViewHolder(private val binding: ItemListMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItem) {
            binding.imgAvatarHome.setImageResource(R.drawable.walikota)
            binding.nameHome.text = item.caption
            binding.usernameHome.text = item.userId
            binding.ivImageItem.setImageResource(R.drawable.sampel)
        }
    }
}