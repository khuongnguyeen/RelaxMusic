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
import com.example.calmsleep.databinding.FragmentSoundsBinding
import com.example.calmsleep.ui.adapter.SoundsAdapter

class SoundsFragment : Fragment() {
    private var binding: FragmentSoundsBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoundsBinding.inflate(inflater, container, false)
        binding?.tabLayout?.setupWithViewPager(binding?.vp)
        binding?.vp?.adapter = SoundsAdapter(context!!, childFragmentManager)

        binding?.ivPicks?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[0].title,MyApp.getHandingData()[0].DataMusic
            )
            binding?.ivPicks?.isEnabled = false
            val enableButton = Runnable {  binding?.ivPicks?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivAmbient?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[4].title,MyApp.getHandingData()[4].DataMusic
            )
            binding?.ivAmbient?.isEnabled = false
            val enableButton = Runnable {  binding?.ivAmbient?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivNature?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[5].title,MyApp.getHandingData()[5].DataMusic
            )
            binding?.ivNature?.isEnabled = false
            val enableButton = Runnable {  binding?.ivNature?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.ivKid?.setOnClickListener {
            (activity as MainActivity).callDialogChill(
                MyApp.getHandingData()[8].title,MyApp.getHandingData()[8].DataMusic
            )
            binding?.ivKid?.isEnabled = false
            val enableButton = Runnable {  binding?.ivKid?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}