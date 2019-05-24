package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Podcast(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("website")
        val website: String? = null,

        @field:SerializedName("thumbnail")
        val thumbnail: String? = null,

        @field:SerializedName("explicit_content")
        val explicitContent: Boolean? = null,

        @field:SerializedName("itunes_id")
        val itunesId: Int? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("earliest_pub_date_ms")
        val earliestPubDateMs: Long? = null,

        @field:SerializedName("language")
        val language: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("genre_ids")
        val genreIds: ArrayList<Int>? = null,

        @field:SerializedName("listennotes_url")
        val listennotesUrl: String? = null,

        @field:SerializedName("total_episodes")
        val totalEpisodes: Int? = null,

        @field:SerializedName("is_claimed")
        val isClaimed: Boolean? = null,

        @field:SerializedName("rss")
        val rss: String? = null,

        @field:SerializedName("looking_for")
        val lookingFor: LookingFor? = null,

        @field:SerializedName("extra")
        val extra: Extra? = null,

        @field:SerializedName("publisher")
        val publisher: String? = null,

        @field:SerializedName("latest_pub_date_ms")
        val latestPubDateMs: Long? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("email")
        val email: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readArrayList(Int::class.java.classLoader) as? ArrayList<Int>,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readParcelable(LookingFor::class.java.classLoader),
            parcel.readParcelable(Extra::class.java.classLoader),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeString(image)
        parcel.writeString(website)
        parcel.writeString(thumbnail)
        parcel.writeValue(explicitContent)
        parcel.writeValue(itunesId)
        parcel.writeString(description)
        parcel.writeValue(earliestPubDateMs)
        parcel.writeString(language)
        parcel.writeString(title)
        parcel.writeList(genreIds)
        parcel.writeString(listennotesUrl)
        parcel.writeValue(totalEpisodes)
        parcel.writeValue(isClaimed)
        parcel.writeString(rss)
        parcel.writeParcelable(lookingFor, flags)
        parcel.writeParcelable(extra, flags)
        parcel.writeString(publisher)
        parcel.writeValue(latestPubDateMs)
        parcel.writeString(id)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Podcast> {
        override fun createFromParcel(parcel: Parcel): Podcast {
            return Podcast(parcel)
        }

        override fun newArray(size: Int): Array<Podcast?> {
            return arrayOfNulls(size)
        }
    }
}