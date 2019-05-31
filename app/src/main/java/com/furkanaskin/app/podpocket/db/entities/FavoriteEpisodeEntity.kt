package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.service.response.EpisodesItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Furkan on 2019-05-18
 */


@Entity(tableName = "Favorites")
data class FavoriteEpisodeEntity(
        @PrimaryKey
        var id: String,
        var image: String?,
        var thumbnail: String?,
        var explicitContent: Boolean?,
        var podcastId: String?,
        var listennotesEditUrl: String?,
        var audioLength: Int?,
        var description: String?,
        var audio: String?,
        var title: String?,
        var pubDateMs: Long?,
        var listennotesUrl: String?,
        var maybeAudioInvalid: Boolean?

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)


    constructor(item: EpisodesItem) : this(
            id = item.id ?: "",
            image = item.id,
            thumbnail = item.thumbnail,
            explicitContent = item.explicitContent,
            listennotesEditUrl = item.listennotesEditUrl,
            podcastId = "",
            audioLength = item.audioLength,
            description = item.description,
            audio = item.audio,
            title = item.title,
            pubDateMs = item.pubDateMs,
            listennotesUrl = item.listennotesUrl,
            maybeAudioInvalid = item.maybeAudioInvalid
    )

    constructor(item: Episode) : this(
            id = item.id ?: "",
            image = item.id,
            thumbnail = item.thumbnail,
            explicitContent = item.explicitContent,
            podcastId = item.podcast?.id,
            listennotesEditUrl = item.listennotesEditUrl,
            audioLength = item.audioLength,
            description = item.description,
            audio = item.audio,
            title = item.title,
            pubDateMs = item.pubDateMs,
            listennotesUrl = item.listennotesUrl,
            maybeAudioInvalid = item.maybeAudioInvalid
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(thumbnail)
        parcel.writeValue(explicitContent)
        parcel.writeString(podcastId)
        parcel.writeString(listennotesEditUrl)
        parcel.writeValue(audioLength)
        parcel.writeString(description)
        parcel.writeString(audio)
        parcel.writeString(title)
        parcel.writeValue(pubDateMs)
        parcel.writeString(listennotesUrl)
        parcel.writeValue(maybeAudioInvalid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EpisodeEntity> {
        override fun createFromParcel(parcel: Parcel): EpisodeEntity {
            return EpisodeEntity(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeEntity?> {
            return arrayOfNulls(size)
        }
    }

    fun formatPubDateMs(): String {
        return getDateTime(pubDateMs ?: 0) ?: ""
    }

    private fun getDateTime(s: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: TypeCastException) {
            e.toString()
        }
    }

}