package com.furkanaskin.app.podpocket.ui.feed

import android.os.Bundle
import android.view.View
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
        childFragmentManager.let { fragmentManager ->

            FeedFragmentPagerAdapter(context, fragmentManager).also {
                mBinding.viewPager.adapter = it
            }
        }

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.buttonNewPost.setOnClickListener {
            navigate(R.id.action_feedFragment_to_newPostFragment)
        }
    }
}