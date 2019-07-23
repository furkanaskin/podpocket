package com.furkanaskin.app.podpocket.ui.splash

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 13.04.2019
 */

class SplashActivityViewModel : BaseViewModel() {

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)
    var afterRegisterSuccess: ObservableField<Boolean> = ObservableField(false)

    init {
        alreadyLogged()
    }

    private fun alreadyLogged() {
        doAsync {
            val firebaseID = mAuth.currentUser?.uid
            val user = firebaseID?.let { db.userDao().getUser(it) }

            if (user != null && mAuth.currentUser?.isEmailVerified == true) {
                loginSuccess.set(true)
            }

            if (!user?.surname.isNullOrEmpty()) {
                afterRegisterSuccess.set(true)
            }
        }
    }
}