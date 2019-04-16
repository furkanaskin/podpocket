package com.furkanaskin.app.podpocket.ui.player

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentPlayerBinding

/**
 * Created by Furkan on 16.04.2019
 */

class PlayerFragment : BaseFragment<PlayerViewModel, FragmentPlayerBinding>(PlayerViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_player

    override fun init() {
        mBinding.viewModel = viewModel
    }
}