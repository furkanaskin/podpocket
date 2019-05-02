package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class Podcasts(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("website")
        val website: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("itunes_id")
        val itunesId: Int? = null,

        @field:SerializedName("explicit_content")
        val explicitContent: Boolean? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("language")
        val language: String? = null,

        @field:SerializedName("earliest_pub_date_ms")
        val earliestPubDateMs: Long? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null,

        @field:SerializedName("total_episodes")
        val totalEpisodes: Int? = null,

        @field:SerializedName("is_claimed")
        val isClaimed: Boolean? = null,

        @field:SerializedName("next_episode_pub_date")
        val nextEpisodePubDate: Long? = null,

        @field:SerializedName("rss")
        val rss: String? = null,

        @field:SerializedName("lastest_pub_date_ms")
        val lastestPubDateMs: Long? = null,

        @field:SerializedName("looking_for")
        val lookingFor: LookingFor? = null,

        @field:SerializedName("genres")
        val genres: List<String?>? = null,

        @field:SerializedName("extra")
        val extra: Extra? = null,

        @field:SerializedName("publisher")
        val publisher: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("episodes")
        val episodes: List<EpisodesItem?>? = null
) {
    fun getTotalEpisodes(): String? {
        return "$totalEpisodes Bölüm"
    }
}