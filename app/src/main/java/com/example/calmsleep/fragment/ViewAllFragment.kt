package com.example.calmsleep.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmsleep.adapter.ChillAdapter
import com.example.calmsleep.databinding.FragmentViewBinding
import com.example.calmsleep.model.DataMusic

class ViewAllFragment(val data: MutableList<DataMusic>) : Fragment() {

    private var binding: FragmentViewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewBinding.inflate(inflater, container,false)
        binding?.rc?.layoutManager = GridLayoutManager(context, 2)
        binding?.rc?.adapter = context?.let { ChillAdapter(it,data) }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}