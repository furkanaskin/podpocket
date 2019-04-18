package com.furkanaskin.app.podpocket.service.response

import com.google.gson.annotations.SerializedName

data class LookingFor(

        @field:SerializedName("cross_promotion")
        val crossPromotion: Boolean? = null,

        @field:SerializedName("sponsors")
        val sponsors: Boolean? = null,

        @field:SerializedName("guests")
        val guests: Boolean? = null,

        @field:SerializedName("cohosts")
        val cohosts: Boolean? = null
)