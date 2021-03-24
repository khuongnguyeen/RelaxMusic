package com.example.calmsleep.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.calmsleep.R
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.adapter.VerticalHomeAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.dialog.PlayerDialog
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.Music
import com.example.calmsleep.model.MusicUtils
import kotlinx.android.synthetic.main.item_music.view.*


class HomeAdapter(val context: Context, val list: MutableList<DataMusic>, val listener: OnItemListener) :
    RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return HomeHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun getItemCount() = list.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.albumText.text = list[position].category_name.split(" ")[0]



        Glide.with(context)
            .load(list[position].mp3_thumbnail_b)
            .apply(RequestOptions().override(200, 200))
            .into(holder.ivImg)

        if(list[position].isCheck){
            holder.ivPlayItem.setImageResource(R.drawable.baseline_pause_circle_white_24dp)
        }else{
            holder.ivPlayItem.setImageResource(R.drawable.baseline_play_circle_white_24dp)
        }

        holder.itemView.setOnClickListener {
            MyApp.ID = list[position].id
            Log.e("Chill_Adapter", list[position].id)
            val intent = Intent(context, NotificationActionService::class.java).setAction("PLAY_START")
            intent.putExtra("IS_PLAY", list[position].id)
            context.sendBroadcast(intent)

            holder.check = false
            for (i in MyApp.getRecently()) {
                if (i.musicId == MyApp.ID) {
                    holder.check = true
                    continue
                }
            }
            if (!holder.check) {
                MyApp.getDB().insertRecently(MusicUtils.getDataId(MyApp.ID)!!)
                MyApp.getRecently().clear()
                MyApp.getRecently().addAll(MyApp.getDB().getRecently())
                holder.check = true
            }
            holder.itemView.isEnabled = false
            val enableButton2 = Runnable { holder.itemView.isEnabled = true }
            Handler().postDelayed(enableButton2, 1000)
            listener.onItemClick(list[position])
        }
    }


    class HomeHolder(view: View) : RecyclerView.ViewHolder(view) {
        var check = false
        val albumText = view.album_text!!
        val ivImg = view.iv_img!!
        val ivPlayItem = view.iv_play_item!!

    }

    interface OnItemListener {
        fun onItemClick(item : DataMusic)
    }
}