package com.example.calmsleep.activity

import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class BaseActivity : AppCompatActivity() {

    companion object {
        var dLocale: Locale? = null
    }
    init {
        updateConfig(this)
    }
    private fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale==Locale("") )
            return
        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
}