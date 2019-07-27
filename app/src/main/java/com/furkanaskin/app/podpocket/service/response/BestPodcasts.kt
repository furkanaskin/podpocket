package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class BestPodcasts(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("page_number")
    val pageNumber: Int? = null,

    @field:SerializedName("channels")
    val channels: List<ChannelsItem?>? = null,

    @field:SerializedName("has_previous")
    val hasPrevious: Boolean? = null,

    @field:SerializedName("parent_id")
    val parentId: Int? = null,

    @field:SerializedName("next_page_number")
    val nextPageNumber: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("has_next")
    val hasNext: Boolean? = null,

    @field:SerializedName("podcasts")
    val podcasts: List<Podcasts?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("listennotes_url")
    val listennotesUrl: String? = null,

    @field:SerializedName("previous_page_number")
    val previousPageNumber: Int? = null
)