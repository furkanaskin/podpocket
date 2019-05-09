package com.furkanaskin.app.podpocket.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivitySplashBinding
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity


class SplashActivity : BaseActivity<SplashActivityViewModel, ActivitySplashBinding>(SplashActivityViewModel::class.java) {

    private var countDownTimer: CountDownTimer? = null

    override fun getLayoutRes() = R.layout.activity_splash

    override fun initViewModel(viewModel: SplashActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                when (viewModel.loginSuccess.get()) {
                    false -> {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                    true -> {
                        startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                        finish()
                    }
                }

            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        countDownTimer?.cancel()
    }


}