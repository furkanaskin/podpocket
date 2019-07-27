package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class Genres(

    @field:SerializedName("genres")
    val genres: List<GenresItem?>? = null
)