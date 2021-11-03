package com.books.app.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.books.app.databinding.SliderItemBinding
import com.books.app.model.response.TopBannerSlide
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    private var topBannerSlides: MutableList<TopBannerSlide> = ArrayList()

    override fun getCount(): Int =
        topBannerSlides.size

    var onItemClickListener: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder =
        SliderViewHolder(
            SliderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        Glide.with(viewHolder.binding.root.context)
            .load(topBannerSlides[position].cover)
            .into(viewHolder.binding.imgvBookCover)
        val topBannerSlide = topBannerSlides[position]
        viewHolder.binding.root.setOnClickListener {
            onItemClickListener?.let { it(topBannerSlide.book_id) }
        }
    }

    fun setTopBannerSlides(topBannerSlides: MutableList<TopBannerSlide>) {
        this.topBannerSlides = topBannerSlides
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(val binding: SliderItemBinding) : ViewHolder(binding.root)

}