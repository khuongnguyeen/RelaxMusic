package com.example.calmsleep.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calmsleep.R
import com.example.calmsleep.databinding.SettingPayDeveloperBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PayDevSetting : BottomSheetDialogFragment()  {

    private lateinit var binding: SettingPayDeveloperBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        R.style.DialogAnimation.also {
            it.also { dialog!!.window!!
                .attributes.windowAnimations = it }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingPayDeveloperBinding.inflate(inflater, container,false)


        binding.ivClose.setOnClickListener {
            dismiss()
        }
        val b = SupportDialog()
        binding.cvLunch.setOnClickListener {
            b.show(childFragmentManager,b.tag)
        }
        binding.cvSand.setOnClickListener {
            b.show(childFragmentManager,b.tag)
        }
        binding.cvCoffee.setOnClickListener {
            b.show(childFragmentManager,b.tag)
        }


        return binding.root
    }




    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }



}