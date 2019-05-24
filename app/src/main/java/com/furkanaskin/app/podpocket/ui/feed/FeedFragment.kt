package com.furkanaskin.app.podpocket.ui.feed

import android.view.animation.AnimationUtils
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

    private val animationFadein by lazy {
        AnimationUtils.loadAnimation(
                this.context,
                R.anim.fade_in
        )
    }

    override fun init() {

        mBinding.textViewMainHeading.startAnimation(animationFadein)
    }

}