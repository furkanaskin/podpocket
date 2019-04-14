package com.furkanaskin.app.podpocket.utils

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Furkan on 14.04.2019
 */

class AnalyticsHelper {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    private fun initialize(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun trackEvent(eventName: String, values: HashMap<String, Any>?) {
        trackWithFabric(eventName, values)

        trackWithFirebase(eventName, values)
        Answers.getInstance().logCustom(CustomEvent("Video Played")
                .putCustomAttribute("Category", "Comedy")
                .putCustomAttribute("Length", 350))
    }

    private fun trackWithFirebase(eventName: String, values: HashMap<String, Any>?) {
        val event = Bundle()
        values?.forEach { (key, value) ->
            if (value is String)
                event.putString(key, value)
            else if (value is Int)
                event.putInt(key, value)
        }

        firebaseAnalytics?.logEvent(eventName, event)
    }

    private fun trackWithFabric(eventName: String, values: HashMap<String, Any>?) {
        var event = CustomEvent(eventName)

        values?.forEach { (key, value) ->
            if (value is String)
                event = event.putCustomAttribute(key, value)
            else if (value is Int)
                event = event.putCustomAttribute(key, value)
        }

        Answers.getInstance().logCustom(event)
    }

    object Provider {
        private var helper = AnalyticsHelper()

        fun initialize(context: Context) {
            helper.initialize(context)
        }

        fun getInstance(): AnalyticsHelper {
            return helper
        }
    }

}