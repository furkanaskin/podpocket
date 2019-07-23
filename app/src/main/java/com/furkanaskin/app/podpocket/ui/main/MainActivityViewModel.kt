package com.furkanaskin.app.podpocket.ui.main

import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Constants

class MainActivityViewModel : BaseViewModel() {

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