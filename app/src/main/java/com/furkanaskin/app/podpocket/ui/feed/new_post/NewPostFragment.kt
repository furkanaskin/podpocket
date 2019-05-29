package com.furkanaskin.app.podpocket.ui.feed.new_post

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentNewPostBinding

/**
 * Created by Furkan on 2019-05-26
 */

class NewPostFragment : BaseFragment<NewPostViewModel, FragmentNewPostBinding>(NewPostViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_new_post

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }


}