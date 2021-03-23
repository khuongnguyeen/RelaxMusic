package com.example.calmsleep.dialog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calmsleep.R
import com.example.calmsleep.activity.BaseActivity
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.databinding.SettingLanguageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class LanguageSetting : BottomSheetDialogFragment()  {

    private var binding: SettingLanguageBinding?=null
    private var currentLanguage = "en"
    private var currentLang: String? = null
    lateinit var locale: Locale

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
        binding = SettingLanguageBinding.inflate(inflater, container,false)
        binding?.ivClose?.setOnClickListener {
            dismiss()
        }
        binding?.lanEn?.setOnClickListener {
            setLanguage("")
            setLocale("")
        }
        binding?.lanVi?.setOnClickListener {
            setLanguage("vi")
            setLocale("vi")
        }

        return binding?.root
    }

    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(context, MainActivity::class.java)
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(context, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    private fun setLanguage(i: String) {
        val sharedPreferences: SharedPreferences = context!!.applicationContext.getSharedPreferences("language", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
       editor.putString("language",i)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}