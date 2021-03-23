package com.example.calmsleep.model

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.calmsleep.R


object Utils {
    @JvmStatic
    @BindingAdapter("setText")
    fun setText(tv: TextView, content: String?) {
        tv.setText(content)
    }


    @JvmStatic
    @BindingAdapter("setImageLink")
    fun setImageLink(iv: ImageView, link: String) {
        val linka = link.replace(" ", "%20")
        Glide.with(iv.context).load(linka)
            .into(iv)
    }


}