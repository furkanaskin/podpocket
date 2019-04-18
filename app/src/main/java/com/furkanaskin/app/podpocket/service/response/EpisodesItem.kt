package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class EpisodesItem(

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("explicit_content")
        val explicitContent: Boolean? = null,

        @field:SerializedName("listennotes_edit_url")
        val listennotesEditUrl: String? = null,

        @field:SerializedName("audio_length")
        val audioLength: Int? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("audio")
        val audio: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("pub_date_ms")
        val pubDateMs: Long? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null,

        @field:SerializedName("maybe_audio_invalid")
        val maybeAudioInvalid: Boolean? = null
)