package com.example.calmsleep.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.CountDownTimer
import androidx.room.Room
import com.example.calmsleep.db.AppDatabase
import com.example.calmsleep.db.DataBaseManager
import com.example.calmsleep.manager.MusicOnlineManager
import com.example.calmsleep.model.FavouriteData
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.Music
import com.example.calmsleep.model.MusicOnlineMp3
import com.example.calmsleep.viewmodel.MusicViewModel
import java.util.*

class MyApp : Application() {

    companion object {
        private val musicViewModel = MusicViewModel()
        private val manager = MusicOnlineManager()
        private lateinit var db: AppDatabase
        @SuppressLint("StaticFieldLeak")
        private lateinit var database: DataBaseManager
        private val musicDataBase = mutableListOf<DataMusic>()
        private val musicDownload = mutableListOf<MusicOnlineMp3>()
        private val favouriteData = mutableListOf<FavouriteData>()
        private val recentlyData = mutableListOf<FavouriteData>()
        private val handingData = mutableListOf<Music>()
        var ID = "0"
        var NOTIFICATION = false
        var AUTODOWNLOAD = false
        var BED = false
        var ISPLAYING = false
        var LOOPING =false
        var TIMER = 0
        var DURATION = 0
        var mCountDownTimer: CountDownTimer? = null
        var mTimerRunning = false
        var mTimeLeftInMillis:Long = 600000
        fun getMusicViewModel() = musicViewModel
        fun getDB() = database
        fun getMusicDownLoad() = musicDownload
        fun getMusicDatabase() = musicDataBase
        fun getFavourites() = favouriteData
        fun getRecently() = recentlyData
        fun getDBLocal() = db
        fun getManager() = manager
        fun getHandingData() = handingData
    }

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = applicationContext.getSharedPreferences("language", Context.MODE_PRIVATE)
        val languageToLoad = sharedPreferences.getString("language", "")
        val locale = Locale(languageToLoad!!)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        database = DataBaseManager(applicationContext)
        DataBaseManager(this).createFavourites()
        DataBaseManager(this).createDownload()
        DataBaseManager(this).createRecently()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name.sqlite")
            .allowMainThreadQueries()
            .build()

    }
}
