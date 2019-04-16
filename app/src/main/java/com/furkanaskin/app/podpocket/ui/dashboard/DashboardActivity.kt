package com.furkanaskin.app.podpocket.ui.dashboard

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>(DashboardViewModel::class.java) {

    override fun initViewModel(viewModel: DashboardViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setupNavigation()

    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.container_fragment)
        bottom_navigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.container_fragment).navigateUp()

}