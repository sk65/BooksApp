package com.books.app.view.fragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.books.app.databinding.ChildListItemBinding
import com.books.app.model.response.Book
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ChildAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
    var onItemClickListener: ((Long) -> Unit)? = null
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder =
        ChildViewHolder(
            ChildListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val book = differ.currentList[position]
        glide.load(book.cover_url).into(holder.binding.imgCover)
        Log.i("dev", "child Adapter ${book.name}")

        holder.binding.tvBookTitle.text = book.name
        holder.binding.root.setOnClickListener {
            onItemClickListener?.let { it(book.id) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size



    inner class ChildViewHolder(val binding: ChildListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}