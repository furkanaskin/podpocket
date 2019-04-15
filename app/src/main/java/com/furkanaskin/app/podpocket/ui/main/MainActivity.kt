package com.furkanaskin.app.podpocket.ui.main

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.furkanaskin.app.podpocket.BuildConfig
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityMainBinding
import io.fabric.sdk.android.Fabric

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java) {
    override fun initViewModel(viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }

}
