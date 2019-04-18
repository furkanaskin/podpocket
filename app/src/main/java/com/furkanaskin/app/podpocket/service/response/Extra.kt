package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class Extra(

        @field:SerializedName("twitter_handle")
        val twitterHandle: String? = null,

        @field:SerializedName("instagram_handle")
        val instagramHandle: String? = null,

        @field:SerializedName("url3")
        val url3: String? = null,

        @field:SerializedName("url1")
        val url1: String? = null,

        @field:SerializedName("url2")
        val url2: String? = null,

        @field:SerializedName("facebook_handle")
        val facebookHandle: String? = null,

        @field:SerializedName("linkedin_url")
        val linkedinUrl: String? = null,

        @field:SerializedName("youtube_url")
        val youtubeUrl: String? = null,

        @field:SerializedName("google_url")
        val googleUrl: String? = null,

        @field:SerializedName("spotify_url")
        val spotifyUrl: String? = null,

        @field:SerializedName("wechat_handle")
        val wechatHandle: String? = null
)