package com.example.calmsleep.viewmodel

import android.content.Context
import android.os.Environment
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicOnlineMp3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.FieldPosition

class MusicViewModel : ViewModel() {

    fun saveInToDatabase(item: DataMusic,context: Context) {
        if (MyApp.getDBLocal().musicOnlineMp3Dao().findOneById(item.id) != null) return
        val path =
            Environment.getDataDirectory().path +
                    File.separator + "data" +
                    File.separator + context.packageName +
                    File.separator +
                    item.mp3_title + ".mp3"
            val input = URL(item.mp3_url).openStream()
            val out = FileOutputStream(path)
            val b = ByteArray(1024)
            var le = input.read(b)
            while (le >= 0) {
                out.write(b, 0, le)
                le = input.read(b)
            }
            out.close()
            input.close()

            val mp3 = MusicOnlineMp3(item.id, item.mp3_title, item.mp3_url,  path, item.id, item.category_name)
            MyApp.getDB().insertDownload(item.mp3_title, item.mp3_url, path, item.id, item.category_name)
            MyApp.getDBLocal().musicOnlineMp3Dao().insertOne(mp3)

    }
}