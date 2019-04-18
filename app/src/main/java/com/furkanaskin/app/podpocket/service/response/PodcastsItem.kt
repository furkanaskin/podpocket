package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class PodcastsItem(

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("publisher")
        val publisher: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null
)