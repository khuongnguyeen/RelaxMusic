package com.example.calmsleep.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.SettingNotifitionPopupBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationPopupSetting : BottomSheetDialogFragment() {

    private var binding: SettingNotifitionPopupBinding? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        R.style.DialogAnimation.also {
            it.also { dialog!!.window!!
                .attributes.windowAnimations = it }
        }
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()
        if (Settings.canDrawOverlays(context)) MyApp.BED = true
        if (MyApp.BED) binding?.swi?.isChecked = true
        if (Settings.canDrawOverlays(context)) binding?.swi?.isChecked = true
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingNotifitionPopupBinding.inflate(inflater, container, false)

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }
        binding?.cvAlarm?.setOnClickListener {
            if (!Settings.canDrawOverlays(context)) {
                val alertDialog = AlertDialog.Builder(context!!).create()
                alertDialog.setTitle("Ứng dụng cần quyền vẽ lên ứng dụng khác, bạn đồng ý cấp quyền chứ ???")
                alertDialog.setButton(
                    Dialog.BUTTON_POSITIVE, resources.getText(R.string.yes)
                ) { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context!!.applicationContext.packageName)
                    )
                    startActivityForResult(intent, 0)
                }
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, resources.getText(R.string.no))
                { dialog, _ ->

                    MyApp.BED = false
                    dialog!!.dismiss()
                    binding?.swi?.isChecked = false

                }
                alertDialog.show()


            }
                setDataLocal(MyApp.BED)

        }


        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setDataLocal(i: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.applicationContext.getSharedPreferences(
                "notificationBed",
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putBoolean("notificationBed", i)
        editor.apply()
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


}