package com.cpastone.governow.home.ui.form

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.governow.R
import com.cpastone.governow.data.model.FormItem
import com.capstone.governow.databinding.ItemFormBinding
import com.cpastone.governow.data.model.Aspiration

class FormAdapter(private var items: List<Aspiration>, private val context: Context) : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFormBinding.inflate(inflater, parent, false)
        return FormViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Aspiration>) {
        items = newItems
        notifyDataSetChanged()
    }

    class FormViewHolder(private val binding: ItemFormBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Aspiration) {
            binding.apply {
                tvTitleItemForm.text = item.title
                Glide.with(ivImageItemForm)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(ivImageItemForm)

                ivImageItemForm.setOnClickListener {
                    val intent = Intent(context, DetailFormActivity::class.java).apply {
                        putExtra("FORM_ITEM_TITLE", item.title)
                        putExtra("FORM_ITEM_IMAGE_RES_ID", item.imageUrl)
                        putExtra("FORM_ITEM_DESCRIPTION", item.description)
                        putExtra("FORM_ITEM_DATE", item.date)
                        putExtra("FORM_ITEM_LOCATION", item.location)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
