package com.books.app.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.books.app.databinding.SlideItemContainerBinding
import com.books.app.model.response.Book
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ViewPagerAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ViewPagerAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onItemClickListener: ((Long) -> Unit)? = null

    private val differCallback = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        SliderViewHolder(
            SlideItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val book = differ.currentList[position]
        holder.binding.apply {
            tvAuthorName.text = book.author
            tvBookName.text = book.name
            glide.load(book.cover_url).into(imgBookCover)
            root.setOnClickListener {
                onItemClickListener?.let { it(book.id) }
            }
        }
    }

    override fun getItemCount(): Int =
        differ.currentList.size


}