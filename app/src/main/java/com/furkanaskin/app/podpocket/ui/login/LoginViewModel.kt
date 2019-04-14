package com.furkanaskin.app.podpocket.ui.login

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 14.04.2019
 */

class LoginViewModel(app : Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }
}