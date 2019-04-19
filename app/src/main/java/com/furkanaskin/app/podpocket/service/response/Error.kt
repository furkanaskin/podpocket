package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Furkan on 18.04.2019
 */

data class Error(
        @SerializedName("errors")
        var errors: List<ErrorData>
)