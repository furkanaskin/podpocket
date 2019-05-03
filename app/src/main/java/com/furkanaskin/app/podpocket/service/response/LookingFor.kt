package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(crossPromotion)
        parcel.writeValue(sponsors)
        parcel.writeValue(guests)
        parcel.writeValue(cohosts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LookingFor> {
        override fun createFromParcel(parcel: Parcel): LookingFor {
            return LookingFor(parcel)
        }

        override fun newArray(size: Int): Array<LookingFor?> {
            return arrayOfNulls(size)
        }
    }
}