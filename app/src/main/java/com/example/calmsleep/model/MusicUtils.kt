package com.example.calmsleep.model

import com.example.calmsleep.application.MyApp

object MusicUtils {

    fun getDataId(id: String): DataMusic? {
        for (i in MyApp.getMusicDatabase()) {
            if (i.id == id) {
                return i
            }
        }
        return null
    }

    fun getPosition(id: String): Int {
        for (i in 0 until MyApp.getMusicDatabase().size) {
            if (MyApp.getMusicDatabase()[i].id == id) {
                return i
            }
        }
        return 0
    }

    fun getId(position: Int): String {
        for (i in 0 until MyApp.getMusicDatabase().size) {
            if (i == position) {
                return MyApp.getMusicDatabase()[i].id
            }
        }
        return ""
    }



    fun secondsToTimer(mil: Int): String {
        var timerString = ""
        var secondString = ""
        val hours = (mil / (1000 * 60 * 60))
        val minutes = ((mil % (1000 * 60 * 60)) / (1000 * 60))
        val seconds = ((mil % (1000 * 60 * 60)) % (1000 * 60) / 1000)
        if (hours > 0) {
            timerString = "$hours : "
        }
        secondString = if (seconds < 10) {
            "0$seconds"
        } else {
            "$seconds"
        }
        timerString = "$timerString$minutes:$secondString"
        return timerString
    }

    fun getDataChild(position: Int, data:MutableList<DataMusic>){
        for (i in MyApp.getHandingData()[position].DataMusic){
            for (j in MyApp.getRecently()){
                if ( i.id == j.musicId){
                    data.add(i)
                }
            }
        }
    }
    fun getDataChildFavourite(position: Int, data:MutableList<DataMusic>){
        for (i in MyApp.getHandingData()[position].DataMusic){
            for (j in MyApp.getFavourites()){
                if ( i.id == j.musicId){
                    data.add(i)
                }
            }
        }
    }
    fun getDataChildDownload(position: Int, data:MutableList<DataMusic>){
        for (i in MyApp.getHandingData()[position].DataMusic){
            for (j in MyApp.getMusicDownLoad()){
                if ( i.id == j.musicId){
                    data.add(i)
                }
            }
        }
    }

}