package com.furkanaskin.app.podpocket.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityDashboardBinding

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>(DashboardViewModel::class.java) {

    override fun initViewModel(viewModel: DashboardViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()

    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.container_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = findNavController(R.id.container_fragment)
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.container_fragment).navigateUp()

}