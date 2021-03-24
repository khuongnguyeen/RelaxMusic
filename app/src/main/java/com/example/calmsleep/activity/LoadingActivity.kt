package com.example.calmsleep.activity

import android.app.Activity
import android.content.*
import android.graphics.Color
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.databinding.LoadingBinding
import com.example.calmsleep.model.Music
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.service.MusicService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type


@Suppress("DEPRECATION")
class LoadingActivity : AppCompatActivity(), Animation.AnimationListener {
    private var binding: LoadingBinding? = null
    private var mCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.loading)
        val ani = AnimationUtils.loadAnimation(this, R.anim.logo)
        ani.setAnimationListener(this)
        binding?.animationView?.startAnimation(ani)

        val ani2 = AnimationUtils.loadAnimation(this, R.anim.loading)
        ani2.setAnimationListener(this)
        binding?.pro?.startAnimation(ani2)
        sttBar()
        getMusicData()

    }

    override fun onStart() {
        super.onStart()
        mCountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivities(arrayOf(intent))
            }
        }.start()

    }

    override fun onStop() {
        super.onStop()
        mCountDownTimer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun sttBar() {
        if (Build.VERSION.SDK_INT in 19..20) WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.setWindowFlag(
            this,
            true
        )
        if (Build.VERSION.SDK_INT >= 19) window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= 21) {
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.setWindowFlag(this, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun Int.setWindowFlag(activity: Activity, on: Boolean) {
        val win: Window = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) winParams.flags = winParams.flags or this else winParams.flags =
            winParams.flags and inv()
        win.attributes = winParams
    }


    override fun onBackPressed() {
    }

    private fun getMusicData() {
        val iS: InputStream = resources.openRawResource(R.raw.api_sleepsound)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        iS.use { iS ->
            val reader: Reader = BufferedReader(InputStreamReader(iS, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        val jsonString: String = writer.toString()
        if (jsonString != "") {
            val gSon = Gson()
            val type: Type = object : TypeToken<MutableList<Music?>?>() {}.type
            MyApp.getHandingData().clear()
            MyApp.getHandingData().addAll(gSon.fromJson<MutableList<Music>>(jsonString, type))
            MyApp.getMusicDatabase().clear()
            for (i in MyApp.getHandingData()) {
                MyApp.getMusicDatabase().addAll(i.DataMusic)
            }


        }
    }

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {

    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

}