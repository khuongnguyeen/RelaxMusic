package com.example.calmsleep.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.ItemChilBinding
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicUtils
import kotlinx.android.synthetic.main.item_chil.view.*

@Suppress("DEPRECATION")
class ChillAdapter(val context: Context, private val list: MutableList<DataMusic>) :
    RecyclerView.Adapter<ChillAdapter.HomeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return HomeHolder(LayoutInflater.from(context).inflate(R.layout.item_chil, parent, false))
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.albumText.text = list[position].category_name.split(" ")[0]
        Glide.with(context)
            .load(list[position].mp3_thumbnail_b)
            .into(holder.sivMusic)
        holder.itemView.setOnClickListener {
            MyApp.ID = list[position].id
            Log.e("Chill_Adapter", list[position].id)
            val intent =
                Intent(context, NotificationActionService::class.java).setAction("PLAY_START")
            intent.putExtra("IS_PLAY", list[position].id)
            context.sendBroadcast(intent)
            holder.ivPlayItem.setImageResource(R.drawable.baseline_pause_circle_white_24dp)
            val enableButton = Runnable { holder.ivPlayItem.setImageResource(R.drawable.baseline_play_circle_white_24dp) }
            Handler().postDelayed(enableButton, 2000)

            holder.check = false
            for (i in MyApp.getRecently()) {
                if (i.musicId == MyApp.ID) {
                    holder. check = true
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
        }





    }

    class HomeHolder(view : View) : RecyclerView.ViewHolder(view) {
        var check = false
        val sivMusic = view.squareImageView!!
        val albumText = view.album_text!!
        val ivPlayItem = view.iv_play_item!!

    }


}