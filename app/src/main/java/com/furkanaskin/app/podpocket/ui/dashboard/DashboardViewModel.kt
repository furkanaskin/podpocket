package com.furkanaskin.app.podpocket.ui.dashboard

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.utils.extensions.logE
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Furkan on 16.04.2019
 */

class DashboardViewModel(app: Application) : BaseViewModel(app) {

    private lateinit var firebaseAuth: FirebaseAuth


    init {
        (app as? Podpocket)?.component?.inject(this)

    }


    fun initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth == null)
            try {
                initFirebase()
                Thread.sleep(500)
            } catch (e: Exception) {
                logE(e.toString())
            }
    }

}