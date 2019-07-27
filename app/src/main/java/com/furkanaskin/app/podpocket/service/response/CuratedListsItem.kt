package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class CuratedListsItem(

    @field:SerializedName("podcasts")
    val podcasts: List<PodcastsItem?>? = null,

    @field:SerializedName("source_domain")
    val sourceDomain: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("pub_date_ms")
    val pubDateMs: Long? = null,

    @field:SerializedName("source_url")
    val sourceUrl: String? = null,

    @field:SerializedName("listennotes_url")
    val listennotesUrl: String? = null
)