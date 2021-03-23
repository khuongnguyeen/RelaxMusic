package com.example.calmsleep.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calmsleep.R
import com.example.calmsleep.databinding.SupportBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SupportDialog : BottomSheetDialogFragment()  {

    private var binding: SupportBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SupportBinding.inflate(inflater, container,false)

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        R.style.DialogAnimation.also {
            it.also { dialog!!.window!!
                .attributes.windowAnimations = it }
        }
    }

}