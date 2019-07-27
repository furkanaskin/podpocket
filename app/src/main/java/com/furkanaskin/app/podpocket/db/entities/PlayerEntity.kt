package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furkanaskin.app.podpocket.core.BaseEntity

/**
 * Created by Furkan on 14.05.2019
 */

@Entity(tableName = "Player")
data class PlayerEntity(
    @PrimaryKey
    var id: Int = 0,
    var episodeId: String?,
    var episodeTitle: String?,
    var podcastTitle: String?,
    var podcastId: String?,
    var explicitContent: Boolean,
    var audio: String?,
    var isPlaying: Boolean
) : BaseEntity(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(episodeId)
        parcel.writeString(episodeTitle)
        parcel.writeString(podcastTitle)
        parcel.writeString(podcastId)
        parcel.writeByte(if (explicitContent) 1 else 0)
        parcel.writeString(audio)
        parcel.writeByte(if (isPlaying) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerEntity> {
        override fun createFromParcel(parcel: Parcel): PlayerEntity {
            return PlayerEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlayerEntity?> {
            return arrayOfNulls(size)
        }
    }
}