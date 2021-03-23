package com.example.calmsleep.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.SetAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetAlarmPop : BottomSheetDialogFragment() {

    private var binding: SetAlarmBinding? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        R.style.DialogAnimation.also {
            it.also { dialog!!.window!!
                .attributes.windowAnimations = it }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SetAlarmBinding.inflate(inflater, container, false)
        binding?.cv30min?.setOnClickListener {
            MyApp.TIMER = 1
            dismiss()
        }
        binding?.cv1hour?.setOnClickListener {
            MyApp.TIMER = 2
            dismiss()
        }
        binding?.cv2hour?.setOnClickListener {
            MyApp.TIMER = 3
            dismiss()
        }
        binding?.cv4hour?.setOnClickListener {
            MyApp.TIMER = 4
            dismiss()
        }
        binding?.cv8hour?.setOnClickListener {
            MyApp.TIMER = 5
            dismiss()
        }
        binding?.cvNoHour?.setOnClickListener {
            MyApp.TIMER = 0
            dismiss()
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val intent = Intent(context, NotificationActionService::class.java).setAction("COUNTDOWN")
        context!!.sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


}