package com.example.calmsleep.manager

import android.media.MediaPlayer
import android.util.Log
import com.example.calmsleep.application.MyApp

class MusicOnlineManager : MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    var mp: MediaPlayer? = null

    fun setPath(path: String) {
        mp?.stop()
        release()
        mp = MediaPlayer()
        mp?.setOnErrorListener(this)
        mp?.setDataSource(path)
        prepareAsync()
    }

    fun setLooping(isKhuong: Boolean) {
        mp?.isLooping = isKhuong
    }

    fun isPlaying():Boolean{
        if (mp == null){
            return false
        }
        return mp!!.isPlaying
    }

    fun getDuration() = mp!!.duration

    fun getCurrentPosition() = mp!!.currentPosition
    //
    private fun release() {
        mp?.release()
        mp = null
    }

    private fun prepareAsync() {
        mp?.setOnPreparedListener(this)
        mp?.prepareAsync()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("MediaManagerOnline", "onError........$extra   , $what ")
        return true
    }

    override fun onPrepared(mp: MediaPlayer?) {
        start()
    }

    fun reset(){
        mp?.reset()
    }

    fun start() {
        mp?.start()
    }

    fun pause() {
        mp?.pause()
    }

    fun stop() {
        mp?.stop()
    }
}