package com.example.calmsleep.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.FragmentAlarmBinding
import com.example.calmsleep.dialog.*
import com.example.calmsleep.model.DataMusic


@Suppress("DEPRECATION")
class AlarmFragment : Fragment() {
    private var binding: FragmentAlarmBinding? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        if (Settings.canDrawOverlays(context)) MyApp.BED = true
        if (MyApp.AUTODOWNLOAD) binding?.swDownload?.isChecked = true
        val a = AlarmSetting()
        val b = BedTimeSetting()
        val c = NotificationPopupSetting()
        val d = PayDevSetting()
        val e = LanguageSetting()

        binding?.cvAlarm?.setOnClickListener {
            a.show(childFragmentManager, a.tag)
            binding?.cvAlarm?.isEnabled = false
            val enableButton = Runnable { binding?.cvAlarm?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.cvBedTime?.setOnClickListener {
            b.show(childFragmentManager, b.tag)
            binding?.cvBedTime?.isEnabled = false
            val enableButton = Runnable { binding?.cvBedTime?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.cvNotification?.setOnClickListener {
            c.show(childFragmentManager, c.tag)
            binding?.cvNotification?.isEnabled = false
            val enableButton = Runnable { binding?.cvNotification?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.cvPayDeveloper?.setOnClickListener {
            rateApp()
            binding?.cvPayDeveloper?.isEnabled = false
            val enableButton = Runnable { binding?.cvPayDeveloper?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.cvAppLanguage?.setOnClickListener {
            e.show(childFragmentManager, e.tag)
            binding?.cvAppLanguage?.isEnabled = false
            val enableButton = Runnable { binding?.cvAppLanguage?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        binding?.cvRateUs?.setOnClickListener {
            rateApp()
            binding?.cvRateUs?.isEnabled = false
            val enableButton = Runnable { binding?.cvRateUs?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }

        binding?.swDownload?.setOnCheckedChangeListener { _, isChecked ->
            MyApp.AUTODOWNLOAD = isChecked
            setDataLocal(MyApp.AUTODOWNLOAD)
        }

        binding?.ivFavourites?.setOnClickListener {
            val data = mutableListOf<DataMusic>()
            for (i in MyApp.getMusicDatabase()) {
                for (j in MyApp.getFavourites()) {
                    if (i.id == j.musicId) {
                        data.add(i)
                    }
                }
            }
            if (data.size == 0) {
                Toast.makeText(context, "The list is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            (activity as MainActivity).callDialogChill("Favourites", data)

            binding?.ivFavourites?.isEnabled = false
            val enableButton = Runnable {  binding?.ivFavourites?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }

        binding?.ivDownload?.setOnClickListener {
            MyApp.getMusicDownLoad().clear()
            MyApp.getMusicDownLoad().addAll(MyApp.getDB().getDownload())
            val data = mutableListOf<DataMusic>()
            for (i in MyApp.getMusicDatabase()) {
                for (j in MyApp.getMusicDownLoad()) {
                    if (i.id == j.musicId) {
                        data.add(i)
                    }
                }
            }
            if (data.size == 0) {
                Toast.makeText(context, "The list is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            (activity as MainActivity).callDialogChill("Download", data)

            binding?.ivDownload?.isEnabled = false
            val enableButton = Runnable {  binding?.ivDownload?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }
        return binding?.root
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details")
            startActivity(rateIntent)
        }
    }

    private fun rateIntentForUrl(url: String): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(String.format("%s?id=%s", url, context!!.packageName))
        )
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent.addFlags(flags)
        return intent
    }

    private fun setDataLocal(i: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.applicationContext.getSharedPreferences(
                "autoDownload",
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putBoolean("downloadFavourites", i)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}