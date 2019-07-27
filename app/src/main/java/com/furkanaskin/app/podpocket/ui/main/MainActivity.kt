package com.furkanaskin.app.podpocket.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivityMainBinding
import com.furkanaskin.app.podpocket.ui.login.LoginActivity
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.uber.autodispose.autoDisposable

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java) {

    override fun initViewModel(viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.activity_main

    override fun onStart() {
        this@MainActivity.overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
        super.onStart()
    }

    override fun onPause() {
        this@MainActivity.overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.mainActivityIntentLiveData.observe(
            this,
            Observer<Int> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Constants.IntentName.LOGIN_ACTIVITY_TYPE, it)
                startActivity(intent)
                finish()
            }
        )

        RxAnimation.together(
            binding.textViewHello.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
            binding.buttonLogin.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
            binding.buttonRegister.fadeOut(Constants.MainAnimationConstants.NO_DURATION)
        )
            .autoDisposable(scopeProvider)
            .subscribe()

        RxAnimation.sequentially(
            binding.textViewHello.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
            binding.buttonLogin.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
            binding.buttonRegister.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION)
        )
            .autoDisposable(scopeProvider)
            .subscribe()
    }
}
