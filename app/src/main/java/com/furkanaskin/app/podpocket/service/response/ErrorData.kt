package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Furkan on 18.04.2019
 */

data class ErrorData(
        @SerializedName("source")
        var data: ErrorSource
)
