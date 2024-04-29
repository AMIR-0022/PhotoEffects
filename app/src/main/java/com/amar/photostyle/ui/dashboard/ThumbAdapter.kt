package com.amar.photostyle.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photostyle.databinding.ListItemThumbBinding
import com.bumptech.glide.Glide

class ThumbAdapter(private var callback: (position: Int, thumb: Thumb) -> Unit):
    RecyclerView.Adapter<ThumbAdapter.ViewHolder>() {

    private var thumbList: List<Thumb> = arrayListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemThumbBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbAdapter.ViewHolder, position: Int) {
        holder.bind(thumbList[position])
    }

    override fun getItemCount(): Int {
        return thumbList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Thumb>) {
        thumbList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemThumbBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(thumb: Thumb) {
            Glide.with(context)
                .load(thumb.cover)
                .into(binding.ivItemImg)

            itemView.apply {
                itemView.setOnClickListener {
                    callback.invoke(adapterPosition, thumb)
                }
            }
        }
    }

}