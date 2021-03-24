package com.example.calmsleep.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmsleep.adapter.ChillAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.FragmentFavouritesBinding
import com.example.calmsleep.model.DataMusic
import com.google.gson.Gson

class FavouritesFragment(val data: MutableList<DataMusic>) : Fragment(),
    ChillAdapter.OnItemListener {

    private var binding: FragmentFavouritesBinding? = null
    private var adapter : ChillAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = GridLayoutManager(context, 2)
        adapter = ChillAdapter(context!!,data,this)
        binding?.rc?.adapter = adapter
        if (data.size > 0) {
            binding?.ivDownload?.visibility = View.GONE
            binding?.txtFav?.visibility = View.GONE
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        context?.  unregisterReceiver(broadcastReceiver)
    }

    override fun onStart() {
        if (data.size > 0) {
            binding?.ivDownload?.visibility = View.GONE
            binding?.txtFav?.visibility = View.GONE
        }
        super.onStart()
        context?.registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
        if (!MyApp.ISPLAYING){
            MyApp.getHandingData().forEach {
                it.DataMusic.forEach { dataMusic ->
                    dataMusic.isCheck = false
                }
            }
            adapter!!.notifyDataSetChanged()
        }
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getString("actionName")!!) {
                "PAUSING" -> {
                    MyApp.getHandingData().forEach {
                        it.DataMusic.forEach { dataMusic ->
                            dataMusic.isCheck = false
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                }

                "PLAYING" -> {
                    MyApp.getHandingData().forEach {
                        it.DataMusic.forEach { dataMusic ->
                            if (dataMusic.id == MyApp.ID){
                                dataMusic.isCheck = true
                            }
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                }

                "CANCEL" -> {
                    MyApp.getHandingData().forEach {
                        it.DataMusic.forEach { dataMusic ->
                            dataMusic.isCheck = false
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                }

            }
        }
    }

    override fun onItemClick(item: DataMusic) {
        Log.e("~~~~~~~~~~~~~", Gson().toJson(item))

        MyApp.getHandingData().forEach {
            it.DataMusic.forEach { dataMusic ->
                dataMusic.isCheck = false
            }
        }
        item.isCheck = true
        adapter!!.notifyDataSetChanged()
    }
}