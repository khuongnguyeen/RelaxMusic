package com.example.calmsleep.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.calmsleep.R
import com.example.calmsleep.activity.MainActivity
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.PlayerBottomSheetBinding
import com.example.calmsleep.model.MusicUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.player_bottom_sheet.*
import java.util.*


@Suppress("DEPRECATION")
class PlayerDialog : BottomSheetDialogFragment() {

    private var bindingPlayer: PlayerBottomSheetBinding?=null
    private var check = false
    var runnable: Runnable? = null
    var handler: Handler? = null



    override fun onStart() {
        Log.e("PlayDialog", "________ onStart")
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.onSaveInstanceState()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        Log.e("PlayDialog", "________ onCreateDialog")
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = true
            }
        }
        bottomSheetDialog.dismissWithAnimation = true
        return BottomSheetDialog(requireContext(), theme).apply { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
    }

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
        handler = Handler()
        Log.e("PlayDialog", "________onCreateView")
        context?.registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
        bindingPlayer = PlayerBottomSheetBinding.inflate(inflater, container, false)
        val linkImage = arguments?.getString("link")
        var checkFavourites = arguments?.getBoolean("favourites", false)
        if (checkFavourites!!) bindingPlayer?.ivIconFavourites?.setImageResource(R.drawable.heart_full)
        checkFavourites = false
        if (MyApp.LOOPING) {
            bindingPlayer?.ivIconLoop?.setImageResource(R.drawable.replay_ic)
        } else {
            bindingPlayer?.ivIconLoop?.setImageResource(R.drawable.replay_infinity_ic)
        }
        bindingPlayer?.progressBar?.max = MyApp.getManager().getDuration()/1000
        runnable = Runnable {
            bindingPlayer?.progressBar!!.progress = MyApp.getManager().getCurrentPosition()/1000
        }
        runnable!!.run()
        handler!!.removeCallbacks(moveSeekBarThread)
        handler!!.postDelayed(moveSeekBarThread, 100)
        if (MyApp.ISPLAYING) {
            if (bindingPlayer != null){
                runnable = Runnable {
                    bindingPlayer?.ivMusicImg!!.animate()
                        .setDuration(60000)
                        .rotationBy(360F)
                        .setInterpolator(LinearInterpolator())
                        .withEndAction(runnable)
                        .start()
                }
                runnable!!.run()
            }
        }else{
            bindingPlayer?.ivIconPlay?.setImageResource(R.drawable.play_ic)
            bindingPlayer?.ivMusicImg!!.animate().cancel()
        }
        val enableButton = Runnable { bindingPlayer?.tvTimeEnd?.text = MusicUtils.secondsToTimer(
            MyApp.getManager().getDuration()
        ) }
        Handler().postDelayed(enableButton, 1000)

        bindingPlayer?.tvSongName?.text = MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)].mp3_title
        bindingPlayer?.tvArtist?.text = MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)].mp3_artist

        Glide.with(this)
            .load(linkImage)
            .into(bindingPlayer?.ivMusicImg!!)

        bindingPlayer?.ivIconPlay?.setOnClickListener {
            val intent = Intent(context, NotificationActionService::class.java).setAction("PLAY")
            context!!.sendBroadcast(intent)
        }

        bindingPlayer?.ivDismiss?.setOnClickListener { dismiss() }

        bindingPlayer?.ivIconLoop?.setOnClickListener {
            if (MyApp.LOOPING) {
                MyApp.getManager().setLooping(false)
                MyApp.LOOPING = false
                setDataLocal(false)
                bindingPlayer?.ivIconLoop?.setImageResource(R.drawable.replay_infinity_ic)
            } else {
                MyApp.getManager().setLooping(true)
                MyApp.LOOPING = true
                setDataLocal(true)
                bindingPlayer?.ivIconLoop?.setImageResource(R.drawable.replay_ic)
            }
        }

        bindingPlayer?.ivIconAlarm?.setOnClickListener {
            val v = SetAlarmPop()
            v.show((activity as MainActivity).supportFragmentManager, v.tag)
        }

        bindingPlayer?.ivIconDownload?.setOnClickListener {
            MainActivity.service!!.downLoadMusic()
        }

        bindingPlayer?.ivIconFavourites?.setOnClickListener {
            check = false
            for (i in MyApp.getFavourites()) {
                if (i.musicId == MyApp.ID) {
                    check = true
                    continue
                }
            }
            if (check) {
                bindingPlayer?.ivIconFavourites?.setImageResource(R.drawable.heart)
                MyApp.getDB().deleteFavourite(MyApp.ID)
                MyApp.getFavourites().clear()
                MyApp.getFavourites().addAll(MyApp.getDB().getFavourites())
                Log.e("okKO", "${MyApp.getFavourites()}")
                check = false
            } else {
                bindingPlayer?.ivIconFavourites?.setImageResource(R.drawable.heart_full)
                MyApp.getDB().insertFavourites(MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)])
                MyApp.getFavourites().clear()
                MyApp.getFavourites().addAll(MyApp.getDB().getFavourites())
                Log.e("okKO", "${MyApp.getFavourites()}")
                if (MyApp.AUTODOWNLOAD) {
                    MainActivity.service!!.downLoadMusic()
                }
                check = true
            }
        }
        return bindingPlayer?.root
    }

    private val moveSeekBarThread: Runnable by lazy {
        object : Runnable {
        override fun run() {
            if(progressBar != null) {
                progressBar.max = MyApp.getManager().getDuration()
                progressBar.progress = MyApp.getManager().getCurrentPosition()
            }

            if (tv_time_start != null){
                tv_time_start.text = MusicUtils.secondsToTimer(
                    MyApp.getManager().getCurrentPosition()
                )
            }
            handler!!.postDelayed(this, 100)
        }
    }
    }

    private fun setDataLocal(i: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.applicationContext.getSharedPreferences(
                "loop",
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putBoolean("loop", i)
        editor.apply()
    }


    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getString("actionName")!!) {

                "PAUSING" -> {
                    bindingPlayer?.ivIconPlay?.setImageResource(R.drawable.play_ic)
                    bindingPlayer?.ivMusicImg!!.animate().cancel()
                }
                "PLAYING" -> {
                    upBroadcast()
                }

                "CANCEL" -> {
                    bindingPlayer?.ivIconPlay?.setImageResource(R.drawable.play_ic)
                    bindingPlayer?.ivMusicImg!!.animate().cancel()
                }

                "COUNTDOWN" -> {
                    startTimer()
                }

            }
        }
    }

    fun upBroadcast(){

        bindingPlayer?.ivIconPlay?.setImageResource(R.drawable.pause_ic)
        runnable = Runnable {
            bindingPlayer?.ivMusicImg!!.animate()
                .setDuration(60000)
                .rotationBy(360F)
                .setInterpolator(LinearInterpolator())
                .withEndAction(runnable)
                .start()
        }
        runnable!!.run()
        bindingPlayer?.tvSongName?.text = MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)].mp3_title
        bindingPlayer?.tvArtist?.text =  MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)].mp3_artist
        Glide.with(context!!)
            .load(MyApp.getMusicDatabase()[MusicUtils.getPosition(MyApp.ID)].mp3_thumbnail_b)
            .into(bindingPlayer?.ivMusicImg!!)
        val enableButton = Runnable {  tv_time_end.text = MusicUtils.secondsToTimer(
            MyApp.getManager().getDuration()
        )}
        Handler().postDelayed(enableButton, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingPlayer?.ivMusicImg!!.animate().cancel()
        context?.unregisterReceiver(broadcastReceiver)
        Log.e("PlayDialog", "________onDestroyView")
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
            override fun onTick(millisUntilFinished: Long) {
                MyApp. mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                MyApp.mTimerRunning = false
            }
        }.start()
        MyApp. mTimerRunning = true
    }

    private fun updateCountDownText() {
        val hour = (MyApp.mTimeLeftInMillis / 3600000).toInt() % 24
        val minutes = (MyApp.mTimeLeftInMillis / 60000).toInt() % 60
        val seconds = (MyApp.mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hour, minutes, seconds)
        bindingPlayer?.countDown?.text = timeLeftFormatted
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingPlayer = null
    }


}