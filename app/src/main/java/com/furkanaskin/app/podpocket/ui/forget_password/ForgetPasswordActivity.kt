package com.furkanaskin.app.podpocket.ui.forget_password

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivityForgetPasswordBinding
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show

/**
 * Created by Furkan on 15.04.2019
 */

class ForgetPasswordActivity : BaseActivity<ForgetPasswordViewModel, ActivityForgetPasswordBinding>(ForgetPasswordViewModel::class.java) {
    override fun getLayoutRes() = R.layout.activity_forget_password

    override fun initViewModel(viewModel: ForgetPasswordViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewType = intent.getIntExtra("TYPE", 0)
        viewModel.type.set(viewType)

        viewModel.showProgress.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.showProgress.get() == true) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        viewModel.sendMailSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showResetPasswordDialog()
            }

        })

        viewModel.sendVerifyMailSucces.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showVerifyEmailDialog()
            }

        })

        when (viewType) {
            Constants.LoginActivityType.EMAIL_VERIFY -> {
                binding.textViewForgetPassword.hide()
                binding.textViewVerifyEmail.show()
            }
            Constants.LoginActivityType.FORGOT_PASS -> {
                binding.textViewVerifyEmail.hide()
                binding.textViewForgetPassword.show()
            }
        }
    }

    private fun showResetPasswordDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.reset_password_dialog)
                .setNeutralButton("Tamam"
                ) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
                .create()
                .show()
    }

    private fun showVerifyEmailDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.verify_email_dialog)
                .setNeutralButton("Tamam"
                ) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
                .create()
                .show()
    }
}