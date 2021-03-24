package com.example.calmsleep.ui.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmsleep.R
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.adapter.VerticalHomeAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.FragmentHomeBinding
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.Music
import com.example.calmsleep.ui.adapter.HomeAdapter
import com.google.gson.Gson

class HomeFragment : Fragment(), VerticalHomeAdapter.IMusic, HomeAdapter.OnItemListener {

    private var binding: FragmentHomeBinding?=null
    private var adapter : VerticalHomeAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VerticalHomeAdapter(context!!, this, this)
        binding?.rc?.adapter = adapter
        return binding?.root
    }

    override fun getCount() = MyApp.getHandingData().size

    override fun getData(position: Int): Music {
        return MyApp.getHandingData()[position]
    }

    override fun onStart() {
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

    override fun onStop() {
        super.onStop()

    }
    override fun onClick(position: Int) {
        (activity as MainActivity).callDialog(position)
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

    override fun onDestroyView() {
        super.onDestroyView()
        context?.  unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
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