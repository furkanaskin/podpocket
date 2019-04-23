package com.furkanaskin.app.podpocket.ui.splash

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/**
 * Created by Furkan on 13.04.2019
 */

class SplashActivityViewModel(app : Application) : BaseViewModel(app){

    var loginSuccess: ObservableField<Boolean> = ObservableField(false)


    private lateinit var mAuth: FirebaseAuth

    @Inject
    lateinit var db: AppDatabase

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun alreadyLogged() {
        initFirebase()
        val firebaseID = mAuth.currentUser?.uid
        var localID = firebaseID?.let { db.userDao().getUser(it) }

        if (localID != null) {
            loginSuccess.set(true)

        }

    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

    }



}