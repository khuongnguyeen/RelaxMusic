package com.example.calmsleep.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calmsleep.R
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.FragmentStoriesBinding
import com.example.calmsleep.ui.adapter.StoriesAdapter

class StoriesFragment : Fragment() {
    private var binding: FragmentStoriesBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)

        binding?.vp?.adapter = StoriesAdapter(context!!, childFragmentManager)
        binding?.tabLayout?.setupWithViewPager(binding?.vp)

        binding?.ivPicks?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[1].title,MyApp.getHandingData()[1].DataMusic
            )
            binding?.ivPicks?.isEnabled = false
            val enableButton = Runnable {  binding?.ivPicks?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivFemale?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[2].title,MyApp.getHandingData()[2].DataMusic
            )
            binding?.ivFemale?.isEnabled = false
            val enableButton = Runnable {  binding?.ivFemale?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivMale?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[6].title,MyApp.getHandingData()[6].DataMusic
            )
            binding?.ivMale?.isEnabled = false
            val enableButton = Runnable {  binding?.ivMale?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivKids?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[10].title,MyApp.getHandingData()[10].DataMusic
            )
            binding?.ivKids?.isEnabled = false
            val enableButton = Runnable {  binding?.ivKids?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}