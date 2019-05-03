package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class ResultsItem(

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("podcast_title_original")
        val podcastTitleOriginal: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("description_original")
        val descriptionOriginal: String? = null,

        @field:SerializedName("itunes_id")
        val itunesId: Int? = null,

        @field:SerializedName("explicit_content")
        val explicitContent: Boolean? = null,

        @field:SerializedName("publisher_highlighted")
        val publisherHighlighted: String? = null,

        @field:SerializedName("podcast_id")
        val podcastId: String? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null,

        @field:SerializedName("title_highlighted")
        val titleHighlighted: String? = null,

        @field:SerializedName("title_original")
        val titleOriginal: String? = null,

        @field:SerializedName("podcast_title_highlighted")
        val podcastTitleHighlighted: String? = null,

        @field:SerializedName("rss")
        val rss: String? = null,

        @field:SerializedName("podcast_listennotes_url")
        val podcastListennotesUrl: String? = null,

        @field:SerializedName("description_highlighted")
        val descriptionHighlighted: String? = null,

        @field:SerializedName("genres")
        val genres: List<Int?>? = null,

        @field:SerializedName("publisher_original")
        val publisherOriginal: String? = null,

        @field:SerializedName("audio_length_sec")
        val audioLength: String? = null,

        @field:SerializedName("audio")
        val audio: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("pub_date_ms")
        val pubDateMs: Long? = null,

        @field:SerializedName("transcripts_highlighted")
        val transcriptsHighlighted: List<Any?>? = null
)