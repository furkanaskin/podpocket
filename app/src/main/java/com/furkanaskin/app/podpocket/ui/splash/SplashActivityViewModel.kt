package com.furkanaskin.app.podpocket.ui.splash

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel

/**
 * Created by Furkan on 13.04.2019
 */

class SplashActivityViewModel(app : Application) : BaseViewModel(app){

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

}