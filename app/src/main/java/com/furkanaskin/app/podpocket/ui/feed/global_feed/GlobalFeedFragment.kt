package com.furkanaskin.app.podpocket.ui.feed.global_feed

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentGlobalFeedBinding

/**
 * Created by Furkan on 2019-05-26
 */

class GlobalFeedFragment : BaseFragment<GlobalFeedViewModel, FragmentGlobalFeedBinding>(GlobalFeedViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_global_feed

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

}