package com.furkanaskin.app.podpocket.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var baseApi: PodpocketAPI

    @Inject
    lateinit var db: AppDatabase


    lateinit var mAuth: FirebaseAuth

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()

    }

    init {
        initFirebase()
    }

}
