package com.furkanaskin.app.podpocket.ui.feed.post_detail

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPostDetailBinding

/**
 * Created by Furkan on 2019-05-26
 */

class PostDetailFragment : BaseFragment<PostDetailViewModel, FragmentPostDetailBinding>(PostDetailViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_post_detail

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }
}