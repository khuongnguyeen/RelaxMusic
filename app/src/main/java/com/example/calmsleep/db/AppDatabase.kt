package com.example.calmsleep.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calmsleep.model.MusicOnlineMp3

@Database(entities = [MusicOnlineMp3::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicOnlineMp3Dao(): MusicOnlineMp3Dao
}