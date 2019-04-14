package com.furkanaskin.app.podpocket.ui.main

import android.app.Application
import android.content.Intent
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.ui.login.LoginActivity
import javax.inject.Inject

class MainActivityViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var db: AppDatabase

    init {
        (app as? Podpocket)?.component?.inject(this)
    }


    fun registerClick() {
        openLoginActivity(Constants.LoginActivityType.REGISTER_TYPE)
    }

    fun loginClick() {
        openLoginActivity(Constants.LoginActivityType.LOGIN_TYPE)
    }

    private fun openLoginActivity(viewType: Int) {
        val intent = Intent(getApplication(), LoginActivity::class.java)
        intent.putExtra(Constants.IntentName.LOGIN_ACTIVITY_TYPE, viewType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)
    }

}