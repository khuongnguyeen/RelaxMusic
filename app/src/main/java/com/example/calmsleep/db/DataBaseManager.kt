package com.example.calmsleep.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.example.calmsleep.model.FavouriteData
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicOnlineMp3
import java.io.File
import java.io.FileOutputStream

class DataBaseManager(private var context: Context) {

    companion object {
        private const val DB_Name = "music_db"
        private const val name_table_1 = "music"
    }

    init {
        copyDatabase()
    }

    private var database: SQLiteDatabase? = null

    private fun copyDatabase() {
        val input = context.assets.open(DB_Name)
        val path =
            Environment.getDataDirectory().path + File.separator + "data" + File.separator + context.packageName + File.separator + "db"
        File(path).mkdir()
        val fullPath = path + File.separator + DB_Name
        if (File(fullPath).exists()) {
            return
        }
        val out = FileOutputStream(fullPath)
        val b = ByteArray(1024)
        var length = input.read(b)
        while (length >= 0) {
            out.write(b, 0, length)
            length = input.read(b)
        }
        input.close()
        out.close()
    }

//    recently

    fun createRecently() {
        val sql = "CREATE TABLE IF NOT EXISTS recently ( " +
                "id INTEGER NOT NULL UNIQUE, " +
                "music_id TEXT  NOT NULL, " +
                "album_id TEXT  NOT NULL, " +
                "PRIMARY KEY(id AUTOINCREMENT) " +
                ")"
        openDatabase()
        database?.execSQL(sql)
        closeDatabase()
    }

    fun createFavourites() {
        val sql = "CREATE TABLE IF NOT EXISTS favourites ( " +
                "id INTEGER NOT NULL UNIQUE, " +
                "music_id TEXT  NOT NULL, " +
                "album_id TEXT  NOT NULL, " +
                "PRIMARY KEY(id AUTOINCREMENT) " +
                ")"
        openDatabase()
        database?.execSQL(sql)
        closeDatabase()
    }

    fun insertDownload(
        songName: String,
        mp3_thumbnail_b: String,
        pathOnline: String,
        idMusic: String,
        idAlbum: String
    ) {
        openDatabase()
        val contentValues = ContentValues()
        contentValues.put("songName", songName)
        contentValues.put("mp3_thumbnail_b", mp3_thumbnail_b)
        contentValues.put("pathOnline", pathOnline)
        contentValues.put("music_id", idMusic)
        contentValues.put("album_id", idAlbum)
        database?.insert("download", null, contentValues)
        closeDatabase()
    }

    fun getDownload(): MutableList<MusicOnlineMp3> {
        val download = mutableListOf<MusicOnlineMp3>()
        openDatabase()
        val sql = "SELECT * FROM download"
        val cursor = database!!.rawQuery(sql, null)
        cursor.moveToFirst()
        val indexId = cursor.getColumnIndex("id")
        val indexSongName = cursor.getColumnIndex("songName")
        val indexLinkMusic = cursor.getColumnIndex("mp3_thumbnail_b")
        val indexPathOnline = cursor.getColumnIndex("pathOnline")
        val indexMusicId = cursor.getColumnIndex("music_id")
        val indexTAlbumId = cursor.getColumnIndex("album_id")
        while (!cursor.isAfterLast) {
            val id = cursor.getString(indexId)
            val songName = cursor.getString(indexSongName)
            val linkMusic = cursor.getString(indexLinkMusic)
            val pathOnline = cursor.getString(indexPathOnline)
            val musicId = cursor.getString(indexMusicId)
            val albumId = cursor.getString(indexTAlbumId)
            download.add(
                MusicOnlineMp3(
                    id = id,
                    musicId = musicId,
                    albumId = albumId,
                    songName = songName,
                    mp3_thumbnail_b = linkMusic,
                    pathOnline = pathOnline
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return download
    }

    fun createDownload() {
        val sql = "CREATE TABLE IF NOT EXISTS download ( " +
                "id INTEGER NOT NULL UNIQUE, " +
                "songName TEXT  NOT NULL, " +
                "mp3_thumbnail_b TEXT  NOT NULL, " +
                "pathOnline TEXT  NOT NULL, " +
                "music_id TEXT  NOT NULL, " +
                "album_id TEXT  NOT NULL, " +
                "PRIMARY KEY(id AUTOINCREMENT) " +
                ")"
        openDatabase()
        database?.execSQL(sql)
        closeDatabase()
    }

    fun deleteFavourite(musicId: String) {
        val sql = "DELETE FROM favourites  WHERE music_id = $musicId"
        openDatabase()
        database?.execSQL(sql)
        closeDatabase()
    }


    fun insertFavourites(dataMusic: DataMusic) {
        openDatabase()
        val contentValues = ContentValues()
        contentValues.put("music_id", dataMusic.id)
        contentValues.put("album_id", dataMusic.category_name)
        database?.insert("favourites", null, contentValues)
        closeDatabase()
    }

    fun getFavourites(): MutableList<FavouriteData> {
        val favourite = mutableListOf<FavouriteData>()
        openDatabase()
        val sql = "SELECT * FROM favourites"
        val cursor = database!!.rawQuery(sql, null)
        cursor.moveToFirst()
        val indexId = cursor.getColumnIndex("id")
        val indexMusicId = cursor.getColumnIndex("music_id")
        val indexTAlbumId = cursor.getColumnIndex("album_id")

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(indexId)
            val musicId = cursor.getString(indexMusicId)
            val albumId = cursor.getString(indexTAlbumId)
            favourite.add(
                FavouriteData(
                    id = id,
                    musicId = musicId,
                    albumId = albumId
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return favourite
    }


    private fun openDatabase() {
        if (database == null || !database!!.isOpen) {
            val path = Environment.getDataDirectory().path +
                    File.separator + "data" +
                    File.separator + context.packageName +
                    File.separator + "db"
            val fullPath = path + File.separator + DB_Name
            database = SQLiteDatabase.openDatabase(
                fullPath, null,
                SQLiteDatabase.OPEN_READWRITE
            )
        }
    }

    private fun closeDatabase() {
        if (database != null && database!!.isOpen) {
            database?.close()
            database = null
        }
    }


    fun insertRecently(dataMusic: DataMusic) {
        openDatabase()
        val contentValues = ContentValues()
        contentValues.put("music_id", dataMusic.id)
        contentValues.put("album_id", dataMusic.category_name)
        database?.insert("recently", null, contentValues)
        closeDatabase()
    }

    fun getRecently(): MutableList<FavouriteData> {
        val recently = mutableListOf<FavouriteData>()
        openDatabase()
        val sql = "SELECT * FROM recently"
        val cursor = database!!.rawQuery(sql, null)
        cursor.moveToFirst()
        val indexId = cursor.getColumnIndex("id")
        val indexMusicId = cursor.getColumnIndex("music_id")
        val indexTAlbumId = cursor.getColumnIndex("album_id")

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(indexId)
            val musicId = cursor.getString(indexMusicId)
            val albumId = cursor.getString(indexTAlbumId)
            recently.add(
                FavouriteData(
                    id = id,
                    musicId = musicId,
                    albumId = albumId
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return recently
    }

}