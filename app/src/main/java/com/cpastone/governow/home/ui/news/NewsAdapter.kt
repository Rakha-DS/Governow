package com.cpastone.governow.home.ui.news

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.cpastone.governow.data.model.NewsItem

class NewsAdapter(private var newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_image_item_news)
        val titleView: TextView = itemView.findViewById(R.id.tv_title_news)
        val usernameView: TextView = itemView.findViewById(R.id.username_news)
        val likeButton: ImageButton = itemView.findViewById(R.id.ib_like)
        val shareButton: ImageButton = itemView.findViewById(R.id.ib_share)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        Glide.with(holder.itemView.context).load(newsItem.image_res).into(holder.imageView)
        holder.titleView.text = newsItem.title
        holder.usernameView.text = newsItem.publisher

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailNewsActivity::class.java).apply {
                putExtra("title", newsItem.title)
                putExtra("imageUrl", newsItem.image_res)
                putExtra("date", newsItem.publisher)
                putExtra("description", "")
            }
            context.startActivity(intent)
        }

        holder.likeButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
        }

        holder.shareButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Shared!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateData(newNewsList: List<NewsItem>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }
}