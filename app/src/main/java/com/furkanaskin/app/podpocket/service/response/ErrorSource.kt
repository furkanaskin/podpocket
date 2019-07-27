package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Furkan on 18.04.2019
 */

data class ErrorSource(
    @SerializedName("pointer")
    var pointer: String? = null,
    @SerializedName("detail")
    var detail: String? = null
)
