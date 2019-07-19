package com.furkanaskin.app.podpocket.ui.main

import android.os.Bundle
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivityMainBinding
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java) {

    private val composite = CompositeDisposable()

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

        RxAnimation.together(
                binding.textViewHello.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
                binding.buttonLogin.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
                binding.buttonRegister.fadeOut(Constants.MainAnimationConstants.NO_DURATION))
                .subscribe().addTo(composite)

        RxAnimation.sequentially(
                binding.textViewHello.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                binding.buttonLogin.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                binding.buttonRegister.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION))
                .subscribe().addTo(composite)

    }
}
