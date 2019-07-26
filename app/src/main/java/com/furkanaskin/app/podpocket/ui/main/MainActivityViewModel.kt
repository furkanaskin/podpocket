package com.furkanaskin.app.podpocket.ui.main

import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {

    var mainActivityIntentLiveData = MutableLiveData<Int>()

    fun registerClick() {
        openLoginActivity(Constants.LoginActivityType.REGISTER_TYPE)
    }

    fun loginClick() {
        openLoginActivity(Constants.LoginActivityType.LOGIN_TYPE)
    }

    private fun openLoginActivity(viewType: Int) {
        mainActivityIntentLiveData.postValue(viewType)
    }
}