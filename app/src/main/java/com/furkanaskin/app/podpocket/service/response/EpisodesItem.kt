package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class EpisodesItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("explicit_content")
    val explicitContent: Boolean? = null,

    @field:SerializedName("listennotes_edit_url")
    val listennotesEditUrl: String? = null,

    @field:SerializedName("audio_length_sec")
    val audioLength: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("audio")
    val audio: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("pub_date_ms")
    val pubDateMs: Long? = null,

    @field:SerializedName("listennotes_url")
    val listennotesUrl: String? = null,

    @field:SerializedName("maybe_audio_invalid")
    val maybeAudioInvalid: Boolean? = null,

    var isSelected: Boolean? = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(thumbnail)
        parcel.writeValue(explicitContent)
        parcel.writeString(listennotesEditUrl)
        parcel.writeValue(audioLength)
        parcel.writeString(description)
        parcel.writeString(audio)
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeValue(pubDateMs)
        parcel.writeString(listennotesUrl)
        parcel.writeValue(maybeAudioInvalid)
        parcel.writeValue(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EpisodesItem> {
        override fun createFromParcel(parcel: Parcel): EpisodesItem {
            return EpisodesItem(parcel)
        }

        override fun newArray(size: Int): Array<EpisodesItem?> {
            return arrayOfNulls(size)
        }
    }

    fun getPubDateMs(): String {
        return getDateTime(pubDateMs ?: 0) ?: ""
    }

    private fun getDateTime(s: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}