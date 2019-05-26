package com.furkanaskin.app.podpocket.ui.feed

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentFeedBinding


/**
 * Created by Furkan on 16.04.2019
 */

class FeedFragment : BaseFragment<FeedViewModel, FragmentFeedBinding>(FeedViewModel::class.java) {

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.fragment_feed

    override fun init() {
        initAdapter()
    }

    private fun initAdapter() {
        fragmentManager?.let { fragmentManager ->

            FeedFragmentPagerAdapter(context, fragmentManager).also {
                mBinding.viewPager.adapter = it
            }
        }

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

}