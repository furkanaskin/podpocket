package com.furkanaskin.app.podpocket.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import java.util.*
import javax.inject.Inject

open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var baseApi: PodpocketAPI

    @Inject
    lateinit var db: AppDatabase

    lateinit var mAuth: FirebaseAuth
    var user: UserEntity? = null

    lateinit var currentLocation: String

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
    }

    init {
        initFirebase()
        getUser()
        getUserLocation()
    }

    fun getUser() {
        doAsync {
            user = mAuth.currentUser?.uid?.let { db.userDao().getUser(it) }
        }
    }

    private fun getUserLocation() {
        currentLocation = Locale.getDefault().country.toLowerCase()

        if (currentLocation.isEmpty())
            currentLocation = "us"
    }

}
