package com.furkanaskin.app.podpocket.ui.splash

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 13.04.2019
 */

class SplashActivityViewModel(app: Application) : BaseViewModel(app) {

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)
    var afterRegisterSuccess: ObservableField<Boolean> = ObservableField(false)

    init {
        (app as? Podpocket)?.component?.inject(this)
        alreadyLogged()
    }

    private fun alreadyLogged() {
        doAsync {
            val firebaseID = mAuth.currentUser?.uid
            val user = firebaseID?.let { db.userDao().getUser(it) }

            if (user != null) {
                loginSuccess.set(true)
            }

            if (!user?.surname.isNullOrEmpty()) {
                afterRegisterSuccess.set(true)
            }
        }
    }

}