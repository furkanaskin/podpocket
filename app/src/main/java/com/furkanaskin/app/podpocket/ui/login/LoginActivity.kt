package com.furkanaskin.app.podpocket.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.databinding.ActivityLoginBinding
import com.furkanaskin.app.podpocket.ui.after_register.AfterRegisterActivity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.jaychang.st.SimpleText
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 14.04.2019
 */

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(LoginViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun initViewModel(viewModel: LoginViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        val simpleText = SimpleText.from(getString(R.string.agreement))
                .first(getString(R.string.agreement_part_first))
                .textColor(R.color.colorPrettyOrange)
                .pressedTextColor(R.color.colorPrettyOrange)
                .onClick(textViewAgreement) { _, _, _ ->
                    showAgreementDialog()
                }
                .first(getString(R.string.agreement_part_second))
                .textColor(R.color.colorPrettyOrange)
                .pressedTextColor(R.color.colorPrettyOrange)
                .onClick(textViewAgreement) { _, _, _ ->
                    showAgreementDialog()
                }
                .first(getString(R.string.agreement_part_third))
                .textColor(R.color.colorPrettyPurple)
                .pressedTextColor(R.color.colorPrettyPurple)
                .onClick(textViewAgreement) { _, _, _ ->
                    showAgreementDialog()
                }
                .first(getString(R.string.agreement_part_fourth))
                .textColor(R.color.colorPrettyOrange)
                .pressedTextColor(R.color.colorPrettyOrange)
                .onClick(textViewAgreement) { _, _, _ ->
                    showAgreementDialog()
                }

        textViewAgreement.text = simpleText

        viewModel.registerSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showRegisterSuccessDialog()
                viewModel.setType(Constants.LoginActivityType.LOGIN_TYPE)
            }

        })

        viewModel.verifySuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.verifySuccess.get() == false) {
                    showVerifyEmailDialog()
                }
            }

        })

        viewModel.loginSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

                doAsync {
                    getUser()

                    runOnUiThread {

                        Thread.sleep(1000)

                        if (user?.surname.isNullOrEmpty()) {
                            Thread.sleep(100)
                            val intent = Intent(this@LoginActivity, AfterRegisterActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Thread.sleep(100)
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

        })

    }

    private fun init() {
        val viewType = intent.getIntExtra(Constants.IntentName.LOGIN_ACTIVITY_TYPE, Constants.LoginActivityType.LOGIN_TYPE)
        viewModel.setType(viewType)


        viewModel.showProgress.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.showProgress.get() == true) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })
    }

    private fun showAgreementDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.agreement_message)
                .setNeutralButton(getString(R.string.okey)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
                .create()
                .show()
    }

    private fun showRegisterSuccessDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.register_success_dialog)
                .setNeutralButton(getString(R.string.okey)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
                .create()
                .show()
    }

    private fun showVerifyEmailDialog() {

        val builder = AlertDialog.Builder(this)
        // Display a message on alert dialog
        builder.setMessage(getString(R.string.send_verify_mail_again))
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            // Do something when user press the positive button
            viewModel.forgetPasswordClicked()
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton(getString(R.string.cancel)) { _, _ ->
            dialog?.dismiss()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}