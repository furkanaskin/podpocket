package com.furkanaskin.app.podpocket.ui.feed.feed_search

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentFeedSearchBinding

/**
 * Created by Furkan on 2019-05-26
 */

class FeedSearchFragment : BaseFragment<FeedSearchViewModel, FragmentFeedSearchBinding>(FeedSearchViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_feed_search

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedNavController = Navigation.findNavController(view)

        mBinding.button.setOnClickListener {
            nestedNavController.navigate(R.id.action_feedSearchFragment_to_newPostFragment)
        }
    }
}