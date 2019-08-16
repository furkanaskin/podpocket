package com.furkanaskin.app.podpocket.ui.forget_password

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject
import timber.log.Timber

/**
 * Created by Furkan on 15.04.2019
 */

class ForgetPasswordViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) :
    BaseViewModel(api, appDatabase) {

    var userName: ObservableField<String> = ObservableField("")
    var sendMailSuccess: ObservableField<Boolean> = ObservableField(false)
    var sendVerifyMailSucces: ObservableField<Boolean> = ObservableField(false)
    var type: ObservableField<Int> = ObservableField(0)

    fun buttonClick() {
        progressLiveData.postValue(true)
        when (type.get()) {
            Constants.LoginActivityType.FORGOT_PASS -> forgetPassword()
            Constants.LoginActivityType.EMAIL_VERIFY -> verifyEmail()
            else -> forgetPassword()
        }
    }

    private fun forgetPassword() {
        if (!userName.get().isNullOrEmpty()) {
            userName.get()?.let {
                mAuth.sendPasswordResetEmail(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendMailSuccess.set(true)
                        progressLiveData.postValue(false)
                    }
                }
            }?.addOnFailureListener {
                Timber.e(it.toString())
                progressLiveData.postValue(false)
            }
        }
    }

    private fun verifyEmail() {
        val mUser = mAuth.currentUser
        mUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendVerifyMailSucces.set(true)
                progressLiveData.postValue(false)
            } else {
                progressLiveData.postValue(false)
            }
        }
    }
}