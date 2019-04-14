package com.furkanaskin.app.podpocket.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.furkanaskin.app.podpocket.utils.AnalyticsHelper

open class BaseViewModel(app: Application) : AndroidViewModel(app) {
    internal fun trackEvent(eventName: String, data: HashMap<String, Any>? = null) {
        AnalyticsHelper.Provider.getInstance().trackEvent(eventName, data)
    }
}
