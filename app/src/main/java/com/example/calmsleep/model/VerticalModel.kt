package com.example.calmsleep.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class DataMusic(
    val cat_id: String? = null,
    val category_image: String,
    val category_image_thumb: String? = null,
    val category_name: String,
    val cid: String? = null,
    val id: String,
    val mp3_artist: String,
    val mp3_description: String? = null,
    val categoryId: Int? = null,
    val mp3_duration: String? = null,
    val mp3_thumbnail_b: String,
    val mp3_thumbnail_s: String? = null,
    val mp3_title: String,
    val mp3_type: String? = null,
    val mp3_url: String,
    val rate_avg: String? = null,
    val total_download: String? = null,
    val total_rate: String? = null,
    val total_records: String? = null,
    val total_views: String? = null,
    var isCheck:Boolean = false
)


data class FavouriteData(
    val id: Int,
    val musicId: String,
    val albumId: String
)

@Entity
data class MusicOnlineMp3(
    @PrimaryKey
    var id: String,
    var songName: String,
    val mp3_thumbnail_b: String,
    var pathOnline: String,
    val musicId: String,
    val albumId: String
)