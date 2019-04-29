package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Extra(

        @field:SerializedName("twitter_handle")
        val twitterHandle: String? = null,

        @field:SerializedName("instagram_handle")
        val instagramHandle: String? = null,

        @field:SerializedName("url3")
        val url3: String? = null,

        @field:SerializedName("url1")
        val url1: String? = null,

        @field:SerializedName("url2")
        val url2: String? = null,

        @field:SerializedName("facebook_handle")
        val facebookHandle: String? = null,

        @field:SerializedName("linkedin_url")
        val linkedinUrl: String? = null,

        @field:SerializedName("youtube_url")
        val youtubeUrl: String? = null,

        @field:SerializedName("google_url")
        val googleUrl: String? = null,

        @field:SerializedName("spotify_url")
        val spotifyUrl: String? = null,

        @field:SerializedName("wechat_handle")
        val wechatHandle: String? = null
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(twitterHandle)
                parcel.writeString(instagramHandle)
                parcel.writeString(url3)
                parcel.writeString(url1)
                parcel.writeString(url2)
                parcel.writeString(facebookHandle)
                parcel.writeString(linkedinUrl)
                parcel.writeString(youtubeUrl)
                parcel.writeString(googleUrl)
                parcel.writeString(spotifyUrl)
                parcel.writeString(wechatHandle)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Extra> {
                override fun createFromParcel(parcel: Parcel): Extra {
                        return Extra(parcel)
                }

                override fun newArray(size: Int): Array<Extra?> {
                        return arrayOfNulls(size)
                }
        }
}