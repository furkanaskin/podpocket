package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Episodes(

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("explicit_content")
        val explicitContent: Boolean? = null,

        @field:SerializedName("listennotes_edit_url")
        val listennotesEditUrl: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null,

        @field:SerializedName("podcast")
        val podcast: Podcast? = null,

        @field:SerializedName("audio_length")
        val audioLength: Int? = null,

        @field:SerializedName("audio")
        val audio: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("pub_date_ms")
        val pubDateMs: Long? = null,

        @field:SerializedName("maybe_audio_invalid")
        val maybeAudioInvalid: Boolean? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Podcast::class.java.classLoader),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(thumbnail)
        parcel.writeValue(explicitContent)
        parcel.writeString(listennotesEditUrl)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeString(listennotesUrl)
        parcel.writeValue(audioLength)
        parcel.writeParcelable(podcast, flags)
        parcel.writeString(audio)
        parcel.writeString(id)
        parcel.writeValue(pubDateMs)
        parcel.writeValue(maybeAudioInvalid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Episodes> {
        override fun createFromParcel(parcel: Parcel): Episodes {
            return Episodes(parcel)
        }

        override fun newArray(size: Int): Array<Episodes?> {
            return arrayOfNulls(size)
        }
    }
}