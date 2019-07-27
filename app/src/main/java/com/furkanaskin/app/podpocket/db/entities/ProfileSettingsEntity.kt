package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import com.furkanaskin.app.podpocket.core.BaseEntity

/**
 * Created by Furkan on 2019-05-22
 */

data class ProfileSettingsEntity(
    var title: String? = null,
    var imageSource: Int? = null
) : BaseEntity(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeValue(imageSource)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileSettingsEntity> {
        override fun createFromParcel(parcel: Parcel): ProfileSettingsEntity {
            return ProfileSettingsEntity(parcel)
        }

        override fun newArray(size: Int): Array<ProfileSettingsEntity?> {
            return arrayOfNulls(size)
        }
    }
}