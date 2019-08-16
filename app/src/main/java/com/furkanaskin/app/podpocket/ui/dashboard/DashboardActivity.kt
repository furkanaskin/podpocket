package com.furkanaskin.app.podpocket.ui.dashboard

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityDashboardBinding
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.contentView
import timber.log.Timber

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>(DashboardViewModel::class.java) {
    var isKeyboardShowing = false

    override fun initViewModel(viewModel: DashboardViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        listenKeyboardVisibility()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.container_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemReselectedListener {
            when (NavHostFragment.findNavController(container_fragment).currentDestination?.id) {
                R.id.homeFragment -> Timber.v("Reselect blocked.")
                R.id.searchFragment -> Timber.v("Reselect blocked.")
                R.id.feedFragment -> Timber.v("Reselect blocked.")
                R.id.profileFragment -> Timber.v("Reselect blocked.")
                else -> NavigationUI.onNavDestinationSelected(it, navController)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = findNavController(R.id.container_fragment)
        if (navController.currentDestination?.id != item?.itemId)
            NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.container_fragment).navigateUp()

    private fun listenKeyboardVisibility() {
        contentView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val rect = Rect()
            contentView?.getWindowVisibleDisplayFrame(rect)
            val screenHeight: Int? = contentView?.rootView?.height
            if (screenHeight != null) {
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15) {
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true
                        binding.bottomNavigation.hide()
                    }
                } else {
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false
                        binding.bottomNavigation.show()
                    }
                }
            }
        }
    }
}