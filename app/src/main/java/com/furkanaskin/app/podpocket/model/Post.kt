package com.furkanaskin.app.podpocket.model

/**
 * Created by Furkan on 2019-05-29
 */

data class Post(val podcaster: Boolean? = null,
                val pubDate: String? = "",
                val verifiedUser: Boolean? = false,
                val userName: String? = "",
                val post: String? = "",
                val postId: String? = "",
                val userUniqueId: String? = "",
                val region: String? = "")