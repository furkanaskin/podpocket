package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class Search(

        @field:SerializedName("took")
        val took: Double? = null,

        @field:SerializedName("total")
        val total: Int? = null,

        @field:SerializedName("count")
        val count: Int? = null,

        @field:SerializedName("next_offset")
        val nextOffset: Int? = null,

        @field:SerializedName("results")
        val results: List<ResultsItem?>? = null
)