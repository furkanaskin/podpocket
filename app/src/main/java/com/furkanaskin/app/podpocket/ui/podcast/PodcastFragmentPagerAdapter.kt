package com.furkanaskin.app.podpocket.ui.podcast

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.furkanaskin.app.podpocket.ui.podcast.episodes.EpisodesFragment
import com.furkanaskin.app.podpocket.ui.podcast.podcast_detail.PodcastDetailFragment

/**
 * Created by Furkan on 2019-07-06
 */

class PodcastFragmentPagerAdapter(
        private val context: Context?,
        fragmentManager: FragmentManager,
        podcast: Podcasts
) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentList = listOf(
            EpisodesFragment.getInstance(podcast),
            PodcastDetailFragment.getInstance(podcast)
    )

    override fun getItem(position: Int): Fragment =
            fragmentList[position]

    override fun getCount(): Int =
            fragmentList.size

    override fun getPageTitle(position: Int): String? =
            context?.resources?.getStringArray(R.array.podcast_titles)?.get(position)
}
