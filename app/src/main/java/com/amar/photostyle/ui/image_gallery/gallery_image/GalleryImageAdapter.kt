package com.amar.photostyle.ui.image_gallery.gallery_image

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photostyle.databinding.ListItemImageBinding

class GalleryImageAdapter(private var callback: (position: Int, image: GalleryImage)-> Unit):
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>(){

    private var imageList: List<GalleryImage> = arrayListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ListItemImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryImageAdapter.ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<GalleryImage>){
        imageList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemImageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(image: GalleryImage) {
            binding.model = image

            itemView.apply {
                setOnClickListener {
                    callback.invoke(adapterPosition, image)
                }
            }
        }
    }

}