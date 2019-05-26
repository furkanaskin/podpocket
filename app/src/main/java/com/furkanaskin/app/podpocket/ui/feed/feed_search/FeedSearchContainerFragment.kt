package com.furkanaskin.app.podpocket.ui.feed.feed_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.furkanaskin.app.podpocket.R

/**
 * Created by Furkan on 2019-05-26
 */

class FeedSearchContainerFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_search_container, container, false)
    }

}