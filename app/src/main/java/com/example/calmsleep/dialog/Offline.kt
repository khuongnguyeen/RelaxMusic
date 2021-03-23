package com.example.calmsleep.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmsleep.R
import com.example.calmsleep.adapter.OfflineAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.ActivityOfflineBinding
import com.example.calmsleep.model.MusicOnlineMp3
import com.example.calmsleep.model.MusicUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


//android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
class Offline(private val str: String, val list: MutableList<MusicOnlineMp3>) :
    BottomSheetDialogFragment(), OfflineAdapter.IMusic {
    var current = 0
    var binding: ActivityOfflineBinding? = null
    private var mp: MediaPlayer? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        R.style.DialogAnimation.also {
            it.also {
                dialog!!.window!!
                    .attributes.windowAnimations = it
            }
        }
    }

    private fun setData(path: String) {
        val intent =
            Intent(context, NotificationActionService::class.java).setAction("CANCEL")
        context!!.sendBroadcast(intent)
        mp?.release()
        mp = MediaPlayer()
        mp?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp2: MediaPlayer?, what: Int, extra: Int): Boolean {
                mp = null
                Log.d("MediaManagerOffline", "error: " + extra)
                return true
            }
        })
        mp?.setDataSource(path)
        mp?.prepare()
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private fun initPermission(position: Int) {
        val permissions = mutableListOf<String>()
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (MusicUtils.checkPermission(context!!, permissions)) {
            setData(list[position].pathOnline)
            mp?.start()
            binding?.btnPlay?.setImageResource(R.drawable.baseline_pause_white_48dp)
        } else {
            MusicUtils.showDialogPermission(context as Activity, permissions, 100)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOfflineBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = GridLayoutManager(context, 2)
        binding?.rc?.adapter = context?.let { OfflineAdapter(it, list, inter = this) }
        binding?.btnPlay?.setOnClickListener {
            if (MyApp.getMusicDownLoad().size == 0) return@setOnClickListener
            if (mp != null){
                if (mp!!.isPlaying) {
                    mp?.pause()
                    binding?.btnPlay?.setImageResource(R.drawable.baseline_play_arrow_white_48dp)
                } else {
                    mp?.start()
                    binding?.btnPlay?.setImageResource(R.drawable.baseline_pause_white_48dp)
                    val intent =
                        Intent(context, NotificationActionService::class.java).setAction("CANCEL")
                    context!!.sendBroadcast(intent)
                }
            }else{
                initPermission(0)
            }
        }
        binding?.textView4?.text = str
        binding?.btnNext?.setOnClickListener {
            if (MyApp.getMusicDownLoad().size == 0) return@setOnClickListener
            if (current == MyApp.getMusicDownLoad().size-1){
                current = 0
            }
           else{
                ++current
            }
            initPermission(current)
        }

        binding?.btnPre?.setOnClickListener {
            if (MyApp.getMusicDownLoad().size == 0) return@setOnClickListener
            if (current == 0){
                current = MyApp.getMusicDownLoad().size -1
            }
            else{
                --current
            }
            initPermission(current)
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

        return binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onClick(position: Int, ivPlayItem: ImageView, itemView: View) {
        current = position
        initPermission(position)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

}