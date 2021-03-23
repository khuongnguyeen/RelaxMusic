package com.example.calmsleep.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmsleep.R
import com.example.calmsleep.adapter.DialogAdapter
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.broadcast.NotificationActionService
import com.example.calmsleep.databinding.DialogViewAllBinding
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


//android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
class ViewAllDialog(private val str: String, val list: MutableList<DataMusic>) :
    BottomSheetDialogFragment(), DialogAdapter.IMusic {

    var binding: DialogViewAllBinding? = null

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
        binding = DialogViewAllBinding.inflate(inflater, container, false)
        binding?.rc?.layoutManager = GridLayoutManager(context, 2)
        binding?.rc?.adapter = context?.let { DialogAdapter(it, list, inter = this) }
        binding?.tvTitle?.text = str

        return binding?.root
    }

    override fun onClick(position: Int, ivPlayItem: ImageView, itemView: View) {
        var check = false
        MyApp.ID = list[position].id
        Log.e("Chill_Adapter", list[position].id)
        ivPlayItem.setImageResource(R.drawable.baseline_pause_circle_white_24dp)
        val enableButton =
            Runnable { ivPlayItem.setImageResource(R.drawable.baseline_play_circle_white_24dp) }
        Handler().postDelayed(enableButton, 2000)
        val intent = Intent(context, NotificationActionService::class.java).setAction("PLAY_START")
        intent.putExtra("IS_PLAY", list[position].id)
        context!!.sendBroadcast(intent)
        for (i in MyApp.getRecently()) {
            if (i.musicId == MyApp.ID) {
                check = true
                continue
            }
        }
        if (!check) {
            MyApp.getDB().insertRecently(MusicUtils.getDataId(MyApp.ID)!!)
            MyApp.getRecently().clear()
            MyApp.getRecently().addAll(MyApp.getDB().getRecently())
        }

        itemView.isEnabled = false
        val enableButton2 = Runnable { itemView.isEnabled = true }
        Handler().postDelayed(enableButton2, 1000)
        dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

}