package com.furkanaskin.app.podpocket.ui.profile

import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding

/**
 * Created by Furkan on 16.04.2019
 */

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(ProfileViewModel::class.java) {
    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }


    override fun getLayoutRes(): Int = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.buttonActionDetail.setOnClickListener {
            navigateAccountDetailScreen()
        }

        mBinding.buttonRecentlyPlayed.setOnClickListener {
            navigateRecentlyPlayedScreen()
        }
    }

    fun navigateAccountDetailScreen() {
        navigate(R.id.action_profileFragment_to_accountDetailFragment)
    }

    fun navigateRecentlyPlayedScreen() {
        navigate(R.id.action_profileFragment_to_recentlyPlayedFragment)
    }

}