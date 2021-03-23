package com.example.calmsleep.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.calmsleep.R
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.dialog.BedDialog
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicUtils


@Suppress("DEPRECATION")
class MusicService : LifecycleService() {

    private var bedDialog: BedDialog? = null
    private var count: CountDownTimer? = null
    private var currentIndex = -1



    @SuppressLint("InlinedApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
        if (intent?.getIntExtra("setting", 0) == 1) {
            createNotification("Have a wonderful day ahead, you :)")

        } else if (intent?.getIntExtra("setting", 0) == 2) {

            createNotification("Sleep now. Boost Productivity Tomorrow :)")
            if (MyApp.BED) {
                bedDialog = BedDialog(applicationContext)
                bedDialog!!.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                bedDialog!!.show()
            }

        } else {
            MyApp.getFavourites().clear()
            MyApp.getFavourites().addAll(MyApp.getDB().getFavourites())
        }
        return START_STICKY
    }

    class MyBinder(val service: MusicService) : Binder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return MyBinder(this)
    }

    fun downLoadMusic() {
        val thread = Thread {
            try {
                MyApp.getMusicViewModel().saveInToDatabase(
                    MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)],
                    applicationContext
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    fun playStart(id: String) {
        val intent = Intent(applicationContext, NotificationActionService::class.java).setAction("PLAYING")
        applicationContext!!.sendBroadcast(intent)
        MyApp.getManager().setPath(MusicUtils.getDataId(id)!!.mp3_url.replace(" ", "%20"))
        MyApp.ISPLAYING = true
    }

    fun play() {
        val intent = Intent(applicationContext, NotificationActionService::class.java).setAction("PLAYING")
        applicationContext!!.sendBroadcast(intent)
        MyApp.getManager().start()
        MyApp.ISPLAYING = true
    }

    fun pause() {
        val intent = Intent(applicationContext, NotificationActionService::class.java).setAction("PAUSING")
        applicationContext!!.sendBroadcast(intent)
        MyApp.getManager().pause()
        MyApp.ISPLAYING = false
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getString("actionName")!!) {
                "PREVIOUS" -> {
                    MyApp.ID = MusicUtils.getId(MusicUtils.getPosition(MyApp.ID) - 1)

                    if (MusicUtils.getPosition(MyApp.ID) == 0) {
                        MyApp.ID = MusicUtils.getId(MyApp.getMusicDatabase().size - 1)
                    }
                    playStart(MyApp.ID)
                    createNotification(MyApp.getMusicDatabase(), R.drawable.ic_pause_black_24dp)
                    MyApp.ISPLAYING = true
                }
                "PLAY" -> if (MyApp.ISPLAYING) {
                    pause()
                    createNotification(
                        MyApp.getMusicDatabase(),
                        R.drawable.ic_play_arrow_black_24dp
                    )
                } else {
                    createNotification(MyApp.getMusicDatabase(), R.drawable.ic_pause_black_24dp)
                    play()
                }
                "NEXT" -> {
                    MyApp.ID = MusicUtils.getId(MusicUtils.getPosition(MyApp.ID) + 1)
                    if (MusicUtils.getPosition(MyApp.ID) == MyApp.getMusicDatabase().size - 1) {
                        MyApp.ID = MusicUtils.getId(0)
                    }
                    playStart(MyApp.ID)
                    createNotification(MyApp.getMusicDatabase(), R.drawable.ic_pause_black_24dp)
                }
                "CANCEL" -> MainActivity.service?.cancel()
                "PLAY_START" -> {
                    createNotification(MyApp.getMusicDatabase(), R.drawable.ic_pause_black_24dp)
                    playStart(MyApp.ID)
                }
                "COUNTDOWN" -> {
                    startTimer()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startTimer() {
        MyApp.mCountDownTimer?.cancel()
        var time: Long = 0
        if (MyApp.TIMER == 0) return
        if (MyApp.TIMER == 1) time = 30 * 60 * 1000
        if (MyApp.TIMER == 2) time = 60 * 60 * 1000
        if (MyApp.TIMER == 3) time = 2 * 60 * 60 * 1000
        if (MyApp.TIMER == 4) time = 4 * 60 * 60 * 1000
        if (MyApp.TIMER == 5) time = 8 * 60 * 60 * 1000
        MyApp.mCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                MyApp.mTimerRunning = false
                val intent = Intent(applicationContext, NotificationActionService::class.java).setAction("CANCEL")
                applicationContext!!.sendBroadcast(intent)
            }
        }.start()
        MyApp.mTimerRunning = true
    }

    private fun createNotification(s: String) {
        createChannel2()
        val no = NotificationCompat.Builder(this, "no")
        no.setSmallIcon(R.drawable.baseline_play_circle_white_24dp)
            .setContentTitle("Calm Sleep")
            .setContentText(s)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(2, no.build())
    }

    @SuppressLint("WrongConstant")
    fun createNotification(data: MutableList<DataMusic>, playButton: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pendingIntentPrevious: PendingIntent?
            val intentPrevious = Intent(applicationContext, NotificationActionService::class.java)
                .setAction("PREVIOUS")
            pendingIntentPrevious = PendingIntent.getBroadcast(
                applicationContext, 0,
                intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val intentPlay = Intent(applicationContext, NotificationActionService::class.java)
                .setAction("PLAY")
            val pendingIntentPlay = PendingIntent.getBroadcast(
                applicationContext, 0,
                intentPlay, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val pendingIntentNext: PendingIntent?

            val intentNext = Intent(applicationContext, NotificationActionService::class.java)
                .setAction("NEXT")
            pendingIntentNext = PendingIntent.getBroadcast(
                applicationContext, 0,
                intentNext, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val pendingIntentCancel: PendingIntent?

            val intentCancel = Intent(applicationContext, NotificationActionService::class.java)
                .setAction("CANCEL")
            pendingIntentCancel = PendingIntent.getBroadcast(
                applicationContext, 0,
                intentCancel, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val myIntent = Intent(applicationContext, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            val testNoti = NotificationCompat.Builder(applicationContext, "noti2")
            val mediaSessionCompat = MediaSessionCompat(applicationContext, "tag")
            val icon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.load)

            val notification = testNoti
                .setSmallIcon(R.drawable.baseline_play_circle_white_24dp)
                .setContentTitle(data[MusicUtils.getPosition(MyApp.ID)].mp3_title)
                .setContentText(data[MusicUtils.getPosition(MyApp.ID)].mp3_artist)
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.ic_skip_previous_black_24dp,
                    "Previous",
                    pendingIntentPrevious
                )
                .addAction(playButton, "Play", pendingIntentPlay)
                .addAction(R.drawable.ic_skip_next_black_24dp, "Next", pendingIntentNext)
                .addAction(R.drawable.bg_cancel, "Cancel", pendingIntentCancel)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2, 3)
                )
                .setLargeIcon(icon)
                .setOngoing(true)
                .setShowWhen(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .build()

            startForeground(1, notification)
        }
    }

    private fun createChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val import = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("no", "Nguyễn Duy Khương", import)
            channel.setSound(null, null)
            channel.description = "no"
            val noti = getSystemService(NotificationManager::class.java)
            noti.createNotificationChannel(channel)
        }
    }

    fun cancel() {
        MyApp.ISPLAYING = false
        stopForeground(true)
        MyApp.getManager().pause()
    }
}