package com.furkanaskin.app.podpocket.ui.main

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityMainBinding
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
                val moveX = textX - logoX - textRectf.width() / 2 - 32f.dpToPx() - 24f.dpToPx()


                Log.v("qqq", " textX : $textX")
                Log.v("qqq", " textY : $textY")
                Log.v("qqq", " logoX : $logoX")
                Log.v("qqq", " logoY : $logoY")
                Log.v("qqq", " moveX : $moveX")
                Log.v("qqq", " moveY : $moveY")


                RxAnimation.sequentially(
                        binding.textViewAppName.fadeOut(0L),
                        binding.imageViewAppLogo.fadeOut(0L),
                        binding.textViewHello.fadeOut(0L),
                        binding.buttonLogin.fadeOut(0L),
                        binding.buttonRegister.fadeOut(0L),
                        binding.imageViewAppLogo.resize(1, 1),
                        binding.imageViewAppLogo.fadeIn(500L),
                        binding.imageViewAppLogo.resize(300, 300),
                        binding.imageViewAppLogo.rotation(360f, 650L),
                        binding.imageViewAppLogo.rotation(0f, 650L),
                        binding.imageViewAppLogo.resize(64, 64),
                        binding.imageViewAppLogo.translation(pixelsToDps(this, moveX.toInt()).toFloat(), pixelsToDps(this, moveY).toFloat()),
                        binding.textViewAppName.fadeIn(500L),
                        binding.textViewHello.fadeIn(1000L),
                        binding.buttonLogin.fadeIn(500L),
                        binding.buttonRegister.fadeIn(500L)

                ).subscribe().addTo(composite)

            }
        }

    }

    private fun Float.dpToPx(): Float =
            this * Resources.getSystem().displayMetrics.density


    private fun pixelsToDps(context: Context, pixels: Int): Int {
        val r = context.resources
        return Math.round(pixels / (r.displayMetrics.densityDpi / 160f))
    }


}
