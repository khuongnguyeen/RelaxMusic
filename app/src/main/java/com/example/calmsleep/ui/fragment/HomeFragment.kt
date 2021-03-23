package com.example.calmsleep.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.adapter.VerticalHomeAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.FragmentHomeBinding
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.Music

class HomeFragment : Fragment(), VerticalHomeAdapter.IMusic {

    private var binding: FragmentHomeBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.rc?.adapter = VerticalHomeAdapter(context!!, this)
        return binding?.root
    }

    override fun getCount() = MyApp.getHandingData().size

    override fun getData(position: Int): Music {
        return MyApp.getHandingData()[position]
    }

    override fun onClick(position: Int) {
        (activity as MainActivity).callDialog(position)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}