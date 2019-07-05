package com.furkanaskin.app.podpocket.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.Constants
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.rotation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.progress_dialog.*

class PodPocketProgressDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            val composite = CompositeDisposable()
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)

            val logo = dialog.imageViewAppLogo

            RxAnimation.sequentially(
                    logo.rotation(360f, Constants.MainAnimationConstants.SHORT_DURATION),
                    logo.rotation(0f, Constants.MainAnimationConstants.NO_DURATION))
                    .repeat()
                    .subscribe().addTo(composite)

            dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}