package com.example.calmsleep.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.calmsleep.R
import com.example.calmsleep.application.MyApp
import com.example.calmsleep.fragment.DownloadFragment
import com.example.calmsleep.fragment.FavouritesFragment
import com.example.calmsleep.fragment.ViewAllFragment
import com.example.calmsleep.model.DataMusic
import com.example.calmsleep.model.MusicUtils

class StoriesAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(
    fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> {
                val data = mutableListOf<DataMusic>()
                MusicUtils.getDataChild(1,data = data)
                MusicUtils.getDataChild(2,data = data)
                MusicUtils.getDataChild(6,data = data)
                MusicUtils.getDataChild(10,data = data)
                ViewAllFragment(data)
            }
            1 -> {

                val data = mutableListOf<DataMusic>()
                MusicUtils.getDataChildFavourite(1,data = data)
                MusicUtils.getDataChildFavourite(2,data = data)
                MusicUtils.getDataChildFavourite(6,data = data)
                MusicUtils.getDataChildFavourite(10,data = data)
                FavouritesFragment(data)
            }
            else -> {
                val data = mutableListOf<DataMusic>()
                MusicUtils.getDataChildDownload(1,data = data)
                MusicUtils.getDataChildDownload(2,data = data)
                MusicUtils.getDataChildDownload(6,data = data)
                MusicUtils.getDataChildDownload(10,data = data)
                DownloadFragment(data)
            }
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> {
                context.resources.getText(R.string.recently_listened)
            }
            1 -> {
                context.resources.getText(R.string.favourites)
            }
            else -> {
                context.resources.getText(R.string.downloads)
            }
        }
    }
}