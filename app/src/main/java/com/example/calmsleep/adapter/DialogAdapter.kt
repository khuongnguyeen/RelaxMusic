 package com.example.calmsleep.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.ItemChilBinding
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.Music
import com.example.calmsleep.model.MusicUtils
import kotlinx.android.synthetic.main.item_chil.view.*

class DialogAdapter(val context: Context,val list: MutableList<DataMusic>, val inter: IMusic) : RecyclerView.Adapter<DialogAdapter.HomeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return  HomeHolder(LayoutInflater.from(context).inflate(R.layout.item_chil, parent, false),inter)
    }

    override fun getItemCount() = list.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.albumText.text = list[position].category_name.split(" ")[0]
        Glide.with(context)
            .load(list[position].mp3_thumbnail_b)
            .into(holder.sivMusic)
        holder.itemView.setOnClickListener {
           inter.onClick(position,holder.ivPlayItem,holder.itemView)

        }
    }

    interface IMusic {
        fun onClick(position: Int,ivPlayItem: ImageView,itemView:View)
    }


    class HomeHolder(view: View, inter: IMusic) : RecyclerView.ViewHolder(view) {
        val sivMusic = view.squareImageView!!
        val albumText = view.album_text!!
        val ivPlayItem = view.iv_play_item!!

    }
}