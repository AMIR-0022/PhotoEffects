package com.amar.photostyle.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File


@BindingAdapter("imageUrl")
fun imageLoader(imageView: ImageView, url: String?) {
    Glide.with(imageView).load(url).into(imageView)
}
@BindingAdapter(value = ["workImage"])
fun loadMyWorkImages(view: AppCompatImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().fitCenter())
        .centerCrop()
        .into(view)
}

@BindingAdapter(value = ["selectedPics"])
fun selectedImages(imageView: ImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(imageView.context)
            .load(File(imageUrl))
            .centerCrop()
            .into(imageView)
    }
}



@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("{imageBitmap}")
fun setImageViewBitmap(iv: ImageView, bitmap: Bitmap?) {
    iv.setImageBitmap(bitmap)
}

