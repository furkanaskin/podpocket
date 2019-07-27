package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class TypeAhead(

    @field:SerializedName("podcasts")
    val podcasts: List<PodcastsItem?>? = null,

    @field:SerializedName("terms")
    val terms: List<String?>? = null,

    @field:SerializedName("genres")
    val genres: List<GenresItem?>? = null
)