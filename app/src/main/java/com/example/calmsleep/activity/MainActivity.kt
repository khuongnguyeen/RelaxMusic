package com.example.calmsleep.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.ActivityMainBinding
import com.example.calmsleep.dialog.Offline
import com.example.calmsleep.dialog.PlayerDialog
import com.example.calmsleep.dialog.ViewAllDialog
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicUtils
import com.example.calmsleep.service.MusicService
import com.example.calmsleep.ui.fragment.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var count: CountDownTimer? = null
    private var notificationManager: NotificationManager? = null
    var k = 1
    private var conn: ServiceConnection? = null

    companion object {
        var service: MusicService? = null
    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApp.NOTIFICATION = false
        MyApp.getMusicDownLoad().clear()
        MyApp.getMusicDownLoad().addAll(MyApp.getDB().getDownload())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val intent = Intent(applicationContext, MusicService::class.java)
        applicationContext!!.startService(intent)
        getDataLocal()
        createConnectService()
        binding?.animationView!!.speed = 0.05f
        if (!MyApp.ISPLAYING) {
            binding?.rlGone?.visibility = View.GONE
        } else {
            updateData()
        }
        MyApp.getRecently().clear()
        MyApp.getRecently().addAll(MyApp.getDB().getRecently())
        sttBar()
        val fr1 = HomeFragment()
        val fr2 = SoundsFragment()
        val fr3 = StoriesFragment()
        val fr4 = MeditationFragment()
        val fr5 = AlarmFragment()
        var active: Fragment? = fr1
        val manager = supportFragmentManager
        binding?.bar?.setOnNavigationItemSelectedListener{item ->
            when(item.itemId){
                R.id.action_home -> {
                    binding?.ivBack?.setImageResource(R.drawable.home_bg)
                    manager.beginTransaction().hide(active!!).show(fr1).commit();
                    active = fr1
                    true
                }
                R.id.action_sounds -> {
                    binding?.ivBack?.setImageResource(R.drawable.sounds_bg)
                    manager.beginTransaction().hide(active!!).show(fr2).commit();
                    active = fr2
                    true
                }
                R.id.action_stories -> {
                    binding?.ivBack?.setImageResource(R.drawable.stories_bg)
                    manager.beginTransaction().hide(active!!).show(fr3).commit();
                    active = fr3
                    true
                }
                R.id.action_meditation -> {
                    binding?.ivBack?.setImageResource(R.drawable.meditation_bg)
                    manager.beginTransaction().hide(active!!).show(fr4).commit();
                    active = fr4
                    true
                }
                R.id.action_alarm -> {
                    binding?.ivBack?.setImageResource(R.drawable.home_bg)
                    manager.beginTransaction().hide(active!!).show(fr5).commit();
                    active = fr5
                    true
                }
                else -> false
            }
        }
        manager.beginTransaction().add(R.id.rc, fr5, "5").hide(fr5).commit()
        manager.beginTransaction().add(R.id.rc, fr4, "4").hide(fr4).commit()
        manager.beginTransaction().add(R.id.rc, fr3, "3").hide(fr3).commit()
        manager.beginTransaction().add(R.id.rc, fr2, "2").hide(fr2).commit()
        manager.beginTransaction().add(R.id.rc, fr1, "1").commit()
        val bottomSheetFragment = PlayerDialog()

        binding?.playingSongContainer?.setOnTouchListener { _, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val bundle = Bundle()
                    bundle.putString("link", MusicUtils.getDataId(MyApp.ID)!!.mp3_thumbnail_b)
                    for (j in MyApp.getFavourites()) {
                        if (MyApp.ID == j.musicId) {
                            bundle.putBoolean("favourites", true)
                        }
                    }
                    for (j in MyApp.getMusicDownLoad()) {
                        if (MyApp.ID == j.musicId) {
                            bundle.putBoolean("download", true)
                        }
                    }
                    bundle.putString(
                        "name",
                        MusicUtils.getDataId(MyApp.ID)!!.mp3_title.split("-")[0]
                    )
                    bundle.putString("artist", MusicUtils.getDataId(MyApp.ID)!!.mp3_artist)
                    bottomSheetFragment.arguments = bundle
                    bottomSheetFragment.show(supportFragmentManager, "bottomSheetFragment")
                    binding?.playingSongContainer!!.isEnabled = false
                    val enableButton = Runnable { binding?.playingSongContainer!!.isEnabled = true }
                    Handler().postDelayed(enableButton, 1000)

                }
            }
            false
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
            registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
            startService(Intent(baseContext, MusicService::class.java))
        }

        binding?.buttonSup?.setOnClickListener {
            rateApp()

            binding?.buttonSup?.isEnabled = false
            val enableButton = Runnable { binding?.buttonSup?.isEnabled = true }
            Handler().postDelayed(enableButton, 1000)
        }

        binding?.playPauseButton?.setOnClickListener {
            val intent =
                Intent(applicationContext, NotificationActionService::class.java).setAction("PLAY")
            applicationContext!!.sendBroadcast(intent)
        }

        binding?.queueButton?.setOnClickListener {
            binding?.rlGone?.visibility = View.GONE
        }
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

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiverCheckInternet, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun rateIntentForUrl(url: String): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(String.format("%s?id=%s", url, applicationContext!!.packageName))
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

    override fun onDestroy() {
        super.onDestroy()
        applicationContext!!.unbindService(conn!!)

        binding = null
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiverCheckInternet)
        unregisterReceiver(broadcastReceiver)
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getString("actionName")!!) {
                "PAUSING" -> {
                    binding?.animationView?.pauseAnimation()
                    binding?.rlGone?.visibility = View.VISIBLE
                    binding?.queueButton?.visibility = View.VISIBLE
                    binding?.playPauseButton?.setImageResource(R.drawable.baseline_play_arrow_white_48dp)
                }
                "PLAYING" -> {
                    updateData()
                }
                "CANCEL" -> {
                    binding?.animationView?.pauseAnimation()
                    binding?.rlGone?.visibility = View.VISIBLE
                    binding?.queueButton?.visibility = View.VISIBLE
                    binding?.playPauseButton?.setImageResource(R.drawable.baseline_play_arrow_white_48dp)
                }

            }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "noti2",
                "Duy Khuong", NotificationManager.IMPORTANCE_LOW
            )
            channel.setSound(null, null)
            notificationManager =
                getSystemService<NotificationManager>(NotificationManager::class.java)
            if (notificationManager != null) {
                notificationManager!!.createNotificationChannel(channel)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun isNetworksAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


    fun updateData() {
        for (a in MyApp.getMusicDatabase()) {
            if (a.id == MyApp.ID) {
                binding?.playingSong?.text = a.mp3_title.split("-")[0]
                binding?.playingArtist?.text = a.mp3_artist
            }
        }
        binding?.rlGone?.visibility = View.VISIBLE
        binding?.queueButton?.visibility = View.GONE
        binding?.playPauseButton?.setImageResource(R.drawable.baseline_pause_white_48dp)
        binding?.animationView?.playAnimation()
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

    fun callDialog(position: Int) {
        val v = ViewAllDialog(
            MyApp.getHandingData()[position].title, MyApp.getHandingData()[position].DataMusic
        )
        v.show(supportFragmentManager, v.tag)
    }

    fun callDialogChill(str: String, data: MutableList<DataMusic>) {
        val v = ViewAllDialog(str, data)
        v.show(supportFragmentManager, v.tag)
    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(R.string.do_you_really_want_to_exit)
        alertDialog.setButton(
            Dialog.BUTTON_POSITIVE, resources.getText(R.string.yes)
        ) { _, _ ->
            finishAffinity()
            exitProcess(0)
        }
        alertDialog.setButton(
            Dialog.BUTTON_NEGATIVE, resources.getText(R.string.no)
        ) { dialog, _ -> dialog!!.dismiss() }
        alertDialog.show()
    }

    //check internet

    private var broadcastReceiverCheckInternet : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            var v :Offline? = null
            when (intent.action) {
                ConnectivityManager.CONNECTIVITY_ACTION -> {
                    if (isNetworksAvailable(applicationContext)) {
                        v?.dismiss()
                    } else {

                        v = Offline("You are OFFLINE !!!",  MyApp.getMusicDownLoad())
                        v.isCancelable = false
                        v.show(supportFragmentManager, v.tag)

                    }
                }

            }
        }
    }



    private fun createConnectService() {
        conn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
                val myBinder = binder as MusicService.MyBinder
                service = myBinder.service
            }
        }
        val intent = Intent()
        intent.setClass(applicationContext!!, MusicService::class.java)
        applicationContext!!.bindService(intent, conn!!, Context.BIND_AUTO_CREATE)
    }

    private fun getDataLocal() {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            "autoDownload",
            Context.MODE_PRIVATE
        )
        val auto = sharedPreferences.getBoolean("downloadFavourites", false)
        MyApp.AUTODOWNLOAD = auto

        val shared: SharedPreferences = applicationContext.getSharedPreferences(
            "notificationBed",
            Context.MODE_PRIVATE
        )
        val bed = shared.getBoolean("notificationBed", false)
        MyApp.BED = bed

        val sharedLoop: SharedPreferences = applicationContext.getSharedPreferences(
            "loop",
            Context.MODE_PRIVATE
        )
        val loop = sharedLoop.getBoolean("loop", true)
        MyApp.LOOPING = loop
    }



}