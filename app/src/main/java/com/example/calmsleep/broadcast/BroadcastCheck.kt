package com.example.calmsleep.broadcast
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calmsleep.service.MusicService

@Suppress("NAME_SHADOWING")
open class BroadcastCheck : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getIntExtra("alarm", 0) == 1) {
            val intent = Intent(context, MusicService::class.java)
            intent.putExtra("setting",1)
            context.startService(intent)
        }
        if (intent.getIntExtra("alarm", 0) == 2) {
            val intent = Intent(context, MusicService::class.java)
            intent.putExtra("setting",2)
            context.startService(intent)
        }

    }

}

