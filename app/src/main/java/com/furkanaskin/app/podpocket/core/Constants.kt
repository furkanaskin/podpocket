package com.furkanaskin.app.podpocket.core

/**
 * Created by Furkan on 14.04.2019
 */

object Constants {
    object IntentName {
        val LOGIN_ACTIVITY_TYPE = "login_activity_type"
        val PLAYER_ACTIVITY_ALL_IDS = "allPodIds"
        val PLAYER_ACTIVITY_POSITION = "position"
        val PLAYER_ACTIVITY_ITEM = "pod"
    }

    object LoginActivityType {
        val LOGIN_TYPE = 0
        val REGISTER_TYPE = 1
        val EMAIL_VERIFY = 2
        val FORGOT_PASS = 3
    }

    object NetworkService {
        val BASE_URL = "https://listen-api.listennotes.com/api/v2/"
    }

    object BundleArguments {
        val PODCAST_ID = "podcastId"
        val CURRENT_EPISODE = "currentEpisode"
    }

    object SearchQuery {
        val EPISODE = "episode"
        val PODCAST = "podcast"
    }
}