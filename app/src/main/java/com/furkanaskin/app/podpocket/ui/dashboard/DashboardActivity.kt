package com.furkanaskin.app.podpocket.ui.dashboard

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>(DashboardViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun initViewModel(viewModel: DashboardViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setupNavigation()

    }


    override fun onBackPressed() {
        when (NavHostFragment.findNavController(container_fragment).currentDestination?.id) {
            R.id.homeFragment -> {
                val builder = AlertDialog.Builder(this@DashboardActivity)
                builder.setMessage("Gerçekten çıkış yapmak istiyor musunuz?")

                builder.setPositiveButton("Evet") { dialog, which ->
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                builder.setNegativeButton("Hayır") { dialog, which ->
                    Toast.makeText(this, "Pencere kapatıldı.", Toast.LENGTH_SHORT).show()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(bottom_navigation, findNavController(this, R.id.container_fragment))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this, R.id.container_fragment).navigateUp()


}