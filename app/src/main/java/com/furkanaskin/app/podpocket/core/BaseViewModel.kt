package com.furkanaskin.app.podpocket.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import javax.inject.Inject

open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    @field:[Inject]
    lateinit var api: PodpocketAPI
}
