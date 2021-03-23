package com.example.calmsleep.dialog

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calmsleep.R
import com.example.calmsleep.broadcast.BroadcastCheck
import com.example.calmsleep.databinding.SettingAlarmBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

@Suppress("DEPRECATION")
class AlarmSetting : BottomSheetDialogFragment() {

    private var binding: SettingAlarmBinding?=null
    private var mon = false
    private var tue = false
    private var wed = false
    private var thu = false
    private var fri = false
    private var sat = false
    private var sun = false

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
        binding = SettingAlarmBinding.inflate(inflater, container, false)
        val calendar = Calendar.getInstance()
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BroadcastCheck::class.java)
        getDataLocal()
        isSelected()
        intent.putExtra("alarm", 1)
        val setting = 1
        binding?.cbMon?.setOnCheckedChangeListener { _, isChecked ->
            mon = isChecked
        }
        binding?.cbTue?.setOnCheckedChangeListener { _, isChecked ->
            tue = isChecked
        }
        binding?.cbWeb?.setOnCheckedChangeListener { _, isChecked ->
            wed = isChecked
        }
        binding?.thu?.setOnCheckedChangeListener { _, isChecked ->
            thu = isChecked
        }
        binding?.cbFri?.setOnCheckedChangeListener { _, isChecked ->
            fri = isChecked
        }
        binding?.sat?.setOnCheckedChangeListener { _, isChecked ->
            sat = isChecked
        }
        binding?.cbSun?.setOnCheckedChangeListener { _, isChecked ->
            sun = isChecked
        }
        binding?.timePicker?.setIs24HourView(true)
        binding?.btnDone?.setOnClickListener {
            if (sun) {
                calendar.set(Calendar.DAY_OF_WEEK, 1)
            }
            if (mon) {
                calendar.set(Calendar.DAY_OF_WEEK, 2)
            }
            if (tue) {
                calendar.set(Calendar.DAY_OF_WEEK, 3)
            }
            if (wed) {
                calendar.set(Calendar.DAY_OF_WEEK, 4)
            }
            if (thu) {
                calendar.set(Calendar.DAY_OF_WEEK, 5)
            }
            if (fri) {
                calendar.set(Calendar.DAY_OF_WEEK, 6)
            }
            if (sat) {
                calendar.set(Calendar.DAY_OF_WEEK, 7)
            }
            calendar.set(Calendar.HOUR_OF_DAY, binding?.timePicker!!.currentHour)
            calendar.set(Calendar.MINUTE, binding?.timePicker!!.currentMinute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val alarmTime: Long = calendar.timeInMillis
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                24 * 60 * 60 * 1000,
                pendingIntent
            )

            val gio = binding?.timePicker!!.currentHour
            val phut = binding?.timePicker!!.currentMinute
            setDataLocal(setting, gio, phut, mon, tue, wed, thu, fri, sat, sun)
            var s = ""
            if (phut < 10) {
                s = "$gio:0$phut"
            } else {
                s = "$gio:$phut"
            }
            binding?.tvTimeSet?.text = s
            Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show()
        }

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        return binding?.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    @SuppressLint("NewApi")
    private fun getDataLocal() {
        val sharedPreferences: SharedPreferences = context!!.applicationContext.getSharedPreferences("alarm", Context.MODE_PRIVATE)
        val gio = sharedPreferences.getInt("gio", 0)
        val phut = sharedPreferences.getInt("phut", 0)
        val mon = sharedPreferences.getBoolean("mon",false)
        val tue = sharedPreferences.getBoolean("tue",false)
        val wed = sharedPreferences.getBoolean("wed",false)
        val thu = sharedPreferences.getBoolean("thu",false)
        val fri = sharedPreferences.getBoolean("fri",false)
        val sat = sharedPreferences.getBoolean("sat",false)
        val sun = sharedPreferences.getBoolean("sun",false)
        var s = ""
        s = if (phut < 10) {
            "$gio:0$phut"
        } else {
            "$gio:$phut"
        }
        binding?.timePicker?.hour = gio
        binding?.timePicker?.minute = phut
        binding?.tvTimeSet?.text = s
        this.mon = mon
        this.tue = tue
        this.wed = wed
        this.thu = thu
        this.fri = fri
        this.sat = sat
        this.sun = sun
    }

    private fun isSelected(){
        if (this.mon){
            binding?.cbMon?.isChecked = true
        }
        if (this.tue){
            binding?.cbTue?.isChecked = true
        }
        if (this.wed){
            binding?.cbWeb?.isChecked = true
        }
        if (this.thu){
            binding?.thu?.isChecked = true
        }
        if (this.fri){
            binding?.cbFri?.isChecked = true
        }
        if (this.sat){
            binding?.sat?.isChecked = true
        }
        if (this.sun){
            binding?.cbSun?.isChecked = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setDataLocal(
        i: Int,
        gio: Int,
        phut: Int,
        mon: Boolean,
        tue: Boolean,
        wed: Boolean,
        thu: Boolean,
        fri: Boolean,
        sat: Boolean,
        sun: Boolean
    ) {
        val sharedPreferences: SharedPreferences =
            context!!.applicationContext.getSharedPreferences(
                "alarm",
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putInt("setting", i)
        editor.putInt("gio", gio)
        editor.putInt("phut", phut)
        editor.putBoolean("mon", mon)
        editor.putBoolean("tue", tue)
        editor.putBoolean("wed", wed)
        editor.putBoolean("thu", thu)
        editor.putBoolean("fri", fri)
        editor.putBoolean("sat", sat)
        editor.putBoolean("sun", sun)
        editor.apply()
    }


}