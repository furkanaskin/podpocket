package com.furkanaskin.app.podpocket.model

/**
 * Created by Furkan on 2019-05-25
 */

data class User(val email: String? = "",
                val podcaster: Boolean? = null,
                val accountCreatedAt: String? = "",
                val verifiedUser: Boolean? = false,
                val userName: String? = "")