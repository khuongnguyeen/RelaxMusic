package com.example.calmsleep.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmsleep.adapter.ChillAdapter
import com.example.calmsleep.databinding.FragmentFavouritesBinding
import com.example.calmsleep.model.DataMusic

class FavouritesFragment(val data: MutableList<DataMusic>) : Fragment() {

    private var binding: FragmentFavouritesBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = GridLayoutManager(context, 2)
        binding?.rc?.adapter = context?.let { ChillAdapter(it,data) }
        if (data.size > 0) {
            binding?.ivDownload?.visibility = View.GONE
            binding?.txtFav?.visibility = View.GONE
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}