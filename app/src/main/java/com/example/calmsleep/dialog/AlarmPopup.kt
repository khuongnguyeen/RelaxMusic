package com.example.calmsleep.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.core.app.ServiceCompat.stopForeground
import com.example.calmsleep.R
import com.example.calmsleep.activity.LoadingActivity
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.databinding.PopupBinding
import com.example.calmsleep.service.MusicService

class AlarmPopup (context: Context) : Dialog(context,R.style.BottomSheetDialog) {

    private var binding: PopupBinding? = null


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = PopupBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.btnOff?.setOnClickListener {
            dismiss()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(MainActivity.service!!,Service.STOP_FOREGROUND_REMOVE)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }

    override fun onStop() {
        super.onStop()
        binding = null
    }

}