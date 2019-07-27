package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class Languages(

    @field:SerializedName("languages")
    val languages: List<String?>? = null
)