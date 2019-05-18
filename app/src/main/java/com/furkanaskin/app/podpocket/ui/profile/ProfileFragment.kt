package com.furkanaskin.app.podpocket.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentProfileBinding
import com.furkanaskin.app.podpocket.ui.main.MainActivity

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

        mBinding.buttonFavorites.setOnClickListener {
            navigateFavoritesScreen()
        }

        mBinding.buttonLogout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun navigateAccountDetailScreen() {
        navigate(R.id.action_profileFragment_to_accountDetailFragment)
    }

    fun navigateRecentlyPlayedScreen() {
        navigate(R.id.action_profileFragment_to_recentlyPlayedFragment)
    }

    fun navigateFavoritesScreen() {
        navigate(R.id.action_profileFragment_to_favoritesFragment)
    }
}