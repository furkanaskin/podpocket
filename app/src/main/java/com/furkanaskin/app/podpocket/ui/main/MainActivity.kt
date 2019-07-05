package com.furkanaskin.app.podpocket.ui.main

import android.graphics.Rect
import android.os.Bundle
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivityMainBinding
import com.furkanaskin.app.podpocket.utils.extensions.dpToPx
import com.furkanaskin.app.podpocket.utils.extensions.pixelsToDps
import com.mikhaellopez.rxanimation.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java) {

    private val composite = CompositeDisposable()
    var listenerFlag: Boolean? = true

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

        binding.imageViewAppLogo.viewTreeObserver.addOnGlobalLayoutListener {

            if (listenerFlag != false) {

                listenerFlag = false
                val textRectf = Rect()
                val logoRectf = Rect()

                binding.textViewAppName.getGlobalVisibleRect(textRectf)
                val textX = textRectf.centerX()
                val textY = textRectf.centerY()

                binding.imageViewAppLogo.getGlobalVisibleRect(logoRectf)
                val logoX = logoRectf.centerX()
                val logoY = logoRectf.centerY()


                val moveY = textY - logoY
                val moveX = textX - logoX - textRectf.width() / 2 - (Constants.MainAnimationConstants.LOGO_WIDTH / 2).dpToPx() - Constants.MainAnimationConstants.MARGIN_BETWEEN_LOGO_AND_APPNAME.dpToPx()

                RxAnimation.together(
                        binding.textViewAppName.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
                        binding.textViewHello.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
                        binding.buttonLogin.fadeOut(Constants.MainAnimationConstants.NO_DURATION),
                        binding.buttonRegister.fadeOut(Constants.MainAnimationConstants.NO_DURATION))
                        .subscribe().addTo(composite)

                RxAnimation.sequentially(
                        binding.imageViewAppLogo.fadeIn(Constants.MainAnimationConstants.LONG_DURATION),
                        binding.imageViewAppLogo.resize(300, 300),
                        binding.imageViewAppLogo.rotation(360f, Constants.MainAnimationConstants.SHORT_DURATION),
                        binding.imageViewAppLogo.rotation(720f, Constants.MainAnimationConstants.SHORT_DURATION),
                        binding.imageViewAppLogo.rotation(1080f, Constants.MainAnimationConstants.SHORT_DURATION),
                        RxAnimation.together(
                                binding.imageViewAppLogo.rotation(1440f, Constants.MainAnimationConstants.LONG_DURATION),
                                binding.imageViewAppLogo.resize(Constants.MainAnimationConstants.LOGO_WIDTH_INT, Constants.MainAnimationConstants.LOGO_HEIGHT_INT),
                                binding.imageViewAppLogo.translation(pixelsToDps(this, moveX.toInt()).toFloat(), pixelsToDps(this, moveY).toFloat())),
                        binding.textViewAppName.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                        binding.textViewHello.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                        binding.buttonLogin.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                        binding.buttonRegister.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION))
                        .subscribe().addTo(composite)

            }
        }
    }
}
