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

class SoundsAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

       return when (position) {
           0 -> {

               val data = mutableListOf<DataMusic>()
               MusicUtils.getDataChild(0,data = data)
               MusicUtils.getDataChild(4,data = data)
               MusicUtils.getDataChild(5,data = data)
               MusicUtils.getDataChild(8,data = data)
               MusicUtils.getDataChild(9,data = data)

               ViewAllFragment(data)
           }
           1 -> {
               val data = mutableListOf<DataMusic>()
               MusicUtils.getDataChildFavourite(0,data = data)
               MusicUtils.getDataChildFavourite(4,data = data)
               MusicUtils.getDataChildFavourite(5,data = data)
               MusicUtils.getDataChildFavourite(8,data = data)
               MusicUtils.getDataChildFavourite(9,data = data)
               FavouritesFragment(data)
           }
           else -> {
               val data = mutableListOf<DataMusic>()
               MusicUtils.getDataChildDownload(0,data = data)
               MusicUtils.getDataChildDownload(4,data = data)
               MusicUtils.getDataChildDownload(5,data = data)
               MusicUtils.getDataChildDownload(8,data = data)
               MusicUtils.getDataChildDownload(9,data = data)
               DownloadFragment(data)
           }
        }
    }



    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence? {
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