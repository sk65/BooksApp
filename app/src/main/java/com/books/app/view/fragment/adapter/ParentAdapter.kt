package com.books.app.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.books.app.databinding.ParrentListItemBinding
import com.books.app.model.GenreList
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ParentAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GenreList>() {
        override fun areItemsTheSame(oldItem: GenreList, newItem: GenreList): Boolean {
            return oldItem.genre == newItem.genre
        }

        override fun areContentsTheSame(oldItem: GenreList, newItem: GenreList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    var onItemClickListener: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder =
        ParentViewHolder(
            ParrentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val genreList = differ.currentList[position]
        val childAdapter = ChildAdapter(glide)
        childAdapter.apply {
            differ.submitList(genreList.books)
            onItemClickListener = this@ParentAdapter.onItemClickListener
        }
        holder.binding.apply {
            tvGroupTitle.text = genreList.genre
            rvNestedList.apply {
                adapter = childAdapter
                layoutManager = LinearLayoutManager(
                    holder.binding.root.context,
                    LinearLayoutManager.HORIZONTAL, false
                )
            }
        }


    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class ParentViewHolder(val binding: ParrentListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}