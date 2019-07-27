package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class GenresItem(

    @field:SerializedName("parent_id")
    val parentId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)