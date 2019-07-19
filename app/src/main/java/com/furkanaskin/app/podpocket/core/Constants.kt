package com.furkanaskin.app.podpocket.core

/**
 * Created by Furkan on 14.04.2019
 */

object Constants {
	object IntentName {
		const val LOGIN_ACTIVITY_TYPE = "login_activity_type"
		const val PLAYER_ACTIVITY_ALL_IDS = "allPodIds"
		const val PLAYER_ACTIVITY_POSITION = "position"
		const val PLAYER_ACTIVITY_ITEM = "pod"
		const val PODCAST_ITEM = "podcast"
	}

	object LoginActivityType {
		const val LOGIN_TYPE = 0
		const val REGISTER_TYPE = 1
		const val EMAIL_VERIFY = 2
		const val FORGOT_PASS = 3
	}

	object NetworkService {
		const val BASE_URL = "https://listen-api.listennotes.com/api/v2/"
		const val API_KEY_HEADER_NAME = "X-ListenAPI-Key"
		const val API_KEY_VALUE = "82e6628b74404fb9a26a934b7d1adfa0"
	}

	object BundleArguments {
		const val PODCAST_ID = "podcastId"
		const val CURRENT_EPISODE = "currentEpisode"
		const val TOTAL_EPISODES = "totalEpisodes"
	}

	object SearchQuery {
		const val EPISODE = "episode"
		const val PODCAST = "podcast"
	}

	object MainAnimationConstants {
		const val LOGO_WIDTH = 64f
		const val MARGIN_BETWEEN_LOGO_AND_APPNAME = 24f
		const val LOGO_WIDTH_INT = 64
		const val LOGO_HEIGHT_INT = 64
		const val SHORT_DURATION = 500L
		const val LONG_DURATION = 1000L
		const val EXTRA_LONG_DURATION = 3000L
		const val NO_DURATION = 0L
	}
}