package com.example.calmsleep.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.calmsleep.R
import com.example.calmsleep.model.Music
import com.example.calmsleep.ui.adapter.HomeAdapter
import kotlinx.android.synthetic.main.item_veti.view.*

class VerticalHomeAdapter(val context: Context, val inter: IMusic) :
    RecyclerView.Adapter<VerticalHomeAdapter.VerticalHomeHolder>() {
    private val viewPool = RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalHomeHolder {

        return VerticalHomeHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_veti,
                parent,
                false
            ), inter
        )
    }

    override fun getItemCount() = inter.getCount()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VerticalHomeHolder, position: Int) {
        val homeAdapter = HomeAdapter(context, inter.getData(position).DataMusic)
        holder.rc.setHasFixedSize(true)
        holder.rc.setRecycledViewPool(viewPool)
        holder.rc.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.rc.adapter = homeAdapter
        holder.tvText.text = inter.getData(position).title

    }


    interface IMusic {
        fun getCount(): Int
        fun getData(position: Int): Music
        fun onClick(position: Int)
    }


    class VerticalHomeHolder(view: View, inter: IMusic) : RecyclerView.ViewHolder(view) {
        private val btnViewAll = view.btn_view_all!!
        val rc = view.rc!!
        val tvText = view.tv_text!!
        val rlAll = view.rl_all!!

        init {
            btnViewAll.setOnClickListener {
                inter.onClick(adapterPosition)
                Log.e(
                    "VerticalHomeAdapter",
                    "-----------------ok adapterPosition: $adapterPosition"
                )

                btnViewAll.isEnabled = false
                val enableButton = Runnable { btnViewAll.isEnabled = true }
                Handler().postDelayed(enableButton, 1000)
            }
        }
    }
}