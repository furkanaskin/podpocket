package com.furkanaskin.app.podpocket.ui.splash

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivitySplashBinding
import com.furkanaskin.app.podpocket.ui.after_register.AfterRegisterActivity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity
import com.furkanaskin.app.podpocket.utils.extensions.dpToPx
import com.furkanaskin.app.podpocket.utils.extensions.pixelsToDps
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.mikhaellopez.rxanimation.resize
import com.mikhaellopez.rxanimation.rotation
import com.mikhaellopez.rxanimation.rotationY
import com.mikhaellopez.rxanimation.translation
import com.uber.autodispose.autoDisposable

class SplashActivity :
    BaseActivity<SplashActivityViewModel, ActivitySplashBinding>(SplashActivityViewModel::class.java) {

    private var listenerFlag: Boolean? = true
    override fun getLayoutRes() = R.layout.activity_splash

    override fun initViewModel(viewModel: SplashActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---------------------------------------------------------------------------------------
        // If you don't know what is going over there please don't replace this block or xml file.
        // ---------------------------------------------------------------------------------------

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
                val moveX =
                    textX - logoX - textRectf.width() / 2 - (Constants.MainAnimationConstants.LOGO_WIDTH / 2).dpToPx() - Constants.MainAnimationConstants.MARGIN_BETWEEN_LOGO_AND_APPNAME.dpToPx()

                RxAnimation.together(
                    binding.textViewAppName.fadeOut(Constants.MainAnimationConstants.NO_DURATION)
                )
                    .autoDisposable(scopeProvider)
                    .subscribe()

                RxAnimation.sequentially(
                    binding.imageViewAppLogo.fadeIn(Constants.MainAnimationConstants.LONG_DURATION),
                    binding.imageViewAppLogo.resize(300, 300),
                    binding.imageViewAppLogo.rotation(7200f, Constants.MainAnimationConstants.EXTRA_LONG_DURATION),
                    RxAnimation.together(
                        binding.imageViewAppLogo.resize(
                            Constants.MainAnimationConstants.LOGO_WIDTH_INT,
                            Constants.MainAnimationConstants.LOGO_HEIGHT_INT
                        ),
                        binding.imageViewAppLogo.translation(
                            pixelsToDps(this, moveX.toInt()).toFloat(),
                            pixelsToDps(this, moveY).toFloat(),
                            duration = Constants.MainAnimationConstants.SHORT_DURATION
                        )
                    ),
                    binding.textViewAppName.fadeIn(Constants.MainAnimationConstants.SHORT_DURATION),
                    binding.imageViewAppLogo.rotationY(360f, Constants.MainAnimationConstants.SHORT_DURATION)
                        .doOnComplete {

                            if (viewModel.loginSuccess.get() == true) {
                                if (viewModel.afterRegisterSuccess.get() == true) {
                                    val intent = Intent(this, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(this, AfterRegisterActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                val intent =
                                    Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                startActivity(intent)
                                finish()
                            }
                        }
                )
                    .autoDisposable(scopeProvider)
                    .subscribe()
            }
        }
    }
}