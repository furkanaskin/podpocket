package com.furkanaskin.app.podpocket.ui.dashboard

import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityDashboardBinding

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>(DashboardViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun initViewModel(viewModel: DashboardViewModel) {
        binding.viewModel = viewModel
    }

}