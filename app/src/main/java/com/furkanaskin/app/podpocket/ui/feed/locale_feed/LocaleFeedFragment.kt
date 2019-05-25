package com.furkanaskin.app.podpocket.ui.feed.locale_feed

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentLocaleFeedBinding

/**
 * Created by Furkan on 2019-05-26
 */

class LocaleFeedFragment : BaseFragment<LocaleFeedViewModel, FragmentLocaleFeedBinding>(LocaleFeedViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_locale_feed

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

}