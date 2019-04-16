package com.furkanaskin.app.podpocket.ui.settings

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentSettingsBinding

/**
 * Created by Furkan on 16.04.2019
 */

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>(SettingsViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_settings
    override fun init() {
        mBinding.viewModel = viewModel
    }
}