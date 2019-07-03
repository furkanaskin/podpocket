package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.service.response.Podcast

/**
 * Created by Furkan on 2019-07-03
 */
@Entity(tableName = "RecentlyPlays")
data class RecentlyPlaysEntity(
        @PrimaryKey
        var id: String,
        var image: String? = null,
        var title: String? = null,
        var publisher: String? = null,
        var explicitContent: Boolean? = null,
        var type: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(title)
        parcel.writeString(publisher)
        parcel.writeValue(explicitContent)
        parcel.writeString(type)
    }

    constructor(podcast: Podcast) : this(
            id = podcast.id ?: "",
            image = podcast.image,
            title = podcast.title,
            publisher = podcast.publisher,
            explicitContent = podcast.explicitContent,
            type = "podcast")

    constructor(episode: Episode) : this(
            id = episode.id ?: "",
            image = episode.image,
            title = episode.title,
            publisher = episode.podcast?.publisher,
            explicitContent = episode.explicitContent,
            type = "episode")


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentlyPlaysEntity> {
        override fun createFromParcel(parcel: Parcel): RecentlyPlaysEntity {
            return RecentlyPlaysEntity(parcel)
        }

        override fun newArray(size: Int): Array<RecentlyPlaysEntity?> {
            return arrayOfNulls(size)
        }
    }
}