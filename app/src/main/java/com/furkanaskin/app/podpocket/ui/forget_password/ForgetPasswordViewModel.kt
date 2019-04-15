package com.furkanaskin.app.podpocket.ui.forget_password

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.utils.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Furkan on 15.04.2019
 */

class ForgetPasswordViewModel(app: Application) : BaseViewModel(app) {

    var userName: ObservableField<String> = ObservableField("")
    var sendMailSuccess: ObservableField<Boolean> = ObservableField(false)
    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    private lateinit var mAuth: FirebaseAuth

    init {
        (app as? Podpocket)?.component?.inject(this)
    }


    fun buttonClick() {
        initFirebase()
        progressBarView.set(true)
        if (!userName.get().isNullOrEmpty()) {
            userName.get()?.let {
                mAuth.sendPasswordResetEmail(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendMailSuccess.set(true)
                        progressBarView.set(false)
                    }
                }
            }?.addOnFailureListener {
                Timber.e(it.toString())
                progressBarView.set(false)
            }
        }
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

        if (mAuth == null)
            try {
                initFirebase()
                Thread.sleep(500)
            } catch (e: Exception) {
                logE(e.toString())
            }
    }


}