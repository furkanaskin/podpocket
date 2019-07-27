package com.furkanaskin.app.podpocket.ui.feed

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.ui.feed.feed_search.FeedSearchContainerFragment
import com.furkanaskin.app.podpocket.ui.feed.global_feed.GlobalFeedContainerFragment
import com.furkanaskin.app.podpocket.ui.feed.locale_feed.LocaleFeedContainerFragment

/**
 * Created by Furkan on 2019-05-26
 */

class FeedFragmentPagerAdapter(
    private val context: Context?,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentList = listOf(
        LocaleFeedContainerFragment(),
        GlobalFeedContainerFragment(),
        FeedSearchContainerFragment()
    )

    override fun getItem(position: Int): Fragment =
        fragmentList[position]

    override fun getCount(): Int =
        fragmentList.size

    override fun getPageTitle(position: Int): String? =
        context?.resources?.getStringArray(R.array.feed_fragment_titles)?.get(position)
}
