package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class CuratedPodcasts(

        @field:SerializedName("total")
        val total: Int? = null,

        @field:SerializedName("page_number")
        val pageNumber: Int? = null,

        @field:SerializedName("has_previous")
        val hasPrevious: Boolean? = null,

        @field:SerializedName("curated_lists")
        val curatedLists: List<CuratedListsItem?>? = null,

        @field:SerializedName("next_page_number")
        val nextPageNumber: Int? = null,

        @field:SerializedName("has_next")
        val hasNext: Boolean? = null,

        @field:SerializedName("previous_page_number")
        val previousPageNumber: Int? = null
)