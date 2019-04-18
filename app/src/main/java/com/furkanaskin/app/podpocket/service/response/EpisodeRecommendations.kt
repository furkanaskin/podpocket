package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class EpisodeRecommendations(

        @field:SerializedName("recommendations")
        val recommendations: List<RecommendationsItem?>? = null
)