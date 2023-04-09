package com.example.trailsurfing.recyclerview

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:srcCompat")
fun ImageView.bindSrc(url: String) {
    Glide
        .with(context)
        .load(url)
        .into(this)

}