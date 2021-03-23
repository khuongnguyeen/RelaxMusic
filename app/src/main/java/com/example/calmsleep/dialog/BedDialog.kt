package com.example.calmsleep.dialog

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.BedDialogBinding

class BedDialog(context:Context) : Dialog(context) {

    private var binding: BedDialogBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = BedDialogBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.clDone?.setOnClickListener {
            dismiss()
        }

    }


    override fun onStop() {
        super.onStop()
        binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }



}