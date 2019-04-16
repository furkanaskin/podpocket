package com.furkanaskin.app.podpocket.ui.profile

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(ProfileViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_profile

    override fun init() {
        mBinding.viewModel = viewModel
    }
}