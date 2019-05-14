package com.furkanaskin.app.podpocket.core

/**
 * Created by Furkan on 14.04.2019
 */

object Constants {
    object IntentName {
        val LOGIN_ACTIVITY_TYPE = "login_activity_type"
    }

    object LoginActivityType {
        val LOGIN_TYPE = 0
        val REGISTER_TYPE = 1
    }

    object NetworkService {
        val BASE_URL = "https://listen-api.listennotes.com/api/v2/"
    }

    object BundleArguments {
        val PODCAST_ID = "podcastId"
        val CURRENT_EPISODE = "currentEpisode"
    }
}