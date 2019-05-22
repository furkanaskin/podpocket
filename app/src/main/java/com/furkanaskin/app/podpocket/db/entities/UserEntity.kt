package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furkanaskin.app.podpocket.core.BaseEntity

/**
 * Created by Furkan on 16.04.2019
 */

@Entity(tableName = "User")
data class UserEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var uniqueId: String? = null,
        var userName: String? = null,
        var email: String? = null,
        var name: String? = null,
        var surname: String? = null,
        var birthday: String? = null,
        var profilePictureUrl: String? = null,
        var mostLovedCategory: String? = null,
        var lastPlayedPodcast: String? = null,
        var lastPlayedEpisode: String? = null) : BaseEntity(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(uniqueId)
        parcel.writeString(userName)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeValue(birthday)
        parcel.writeValue(profilePictureUrl)
        parcel.writeString(mostLovedCategory)
        parcel.writeString(lastPlayedPodcast)
        parcel.writeString(lastPlayedEpisode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }
}
