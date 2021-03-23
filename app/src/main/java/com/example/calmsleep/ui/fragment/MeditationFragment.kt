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
import com.example.calmsleep.databinding.FragmentMeditationBinding
import com.example.calmsleep.ui.adapter.MeditationAdapter

class MeditationFragment : Fragment(){
    private var binding: FragmentMeditationBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeditationBinding.inflate(inflater, container,false)
        binding?.vp?.adapter = MeditationAdapter(context!! ,childFragmentManager)
        binding?.tabLayout?.setupWithViewPager(binding?.vp)
        binding?.ivPicks?.setOnClickListener {
            (activity as MainActivity).callDialogChill(MyApp.getHandingData()[7].title,MyApp.getHandingData()[7].DataMusic)
            binding?.ivPicks?.isEnabled = false
            val enableButton = Runnable {  binding?.ivPicks?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivFemale?.setOnClickListener {
            (activity as MainActivity).callDialogChill(MyApp.getHandingData()[11].title,MyApp.getHandingData()[11].DataMusic)
            binding?.ivFemale?.isEnabled = false
            val enableButton = Runnable {  binding?.ivFemale?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivMale?.setOnClickListener {
            (activity as MainActivity).callDialogChill(MyApp.getHandingData()[3].title,MyApp.getHandingData()[3].DataMusic)
            binding?.ivMale?.isEnabled = false
            val enableButton = Runnable {  binding?.ivMale?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}