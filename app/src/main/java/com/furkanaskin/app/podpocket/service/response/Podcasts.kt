package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Podcasts(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("website")
    val website: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("itunes_id")
    val itunesId: Int? = null,

    @field:SerializedName("explicit_content")
    val explicitContent: Boolean? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("earliest_pub_date_ms")
    val earliestPubDateMs: Long? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("listennotes_url")
    val listennotesUrl: String? = null,

    @field:SerializedName("total_episodes")
    val totalEpisodes: Int? = null,

    @field:SerializedName("is_claimed")
    val isClaimed: Boolean? = null,

    @field:SerializedName("next_episode_pub_date")
    val nextEpisodePubDate: Long? = null,

    @field:SerializedName("rss")
    val rss: String? = null,

    @field:SerializedName("lastest")
    val lastestPubDateMs: Long? = null,

    @field:SerializedName("looking_for")
    val lookingFor: LookingFor? = null,

    @field:SerializedName("genres")
    val genres: List<String?>? = null,

    @field:SerializedName("extra")
    val extra: Extra? = null,

    @field:SerializedName("publisher")
    val publisher: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("episodes")
    val episodes: List<EpisodesItem?>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(LookingFor::class.java.classLoader),
        parcel.createStringArrayList(),
        parcel.readParcelable(Extra::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(EpisodesItem)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeString(image)
        parcel.writeString(website)
        parcel.writeString(thumbnail)
        parcel.writeValue(itunesId)
        parcel.writeValue(explicitContent)
        parcel.writeString(description)
        parcel.writeString(language)
        parcel.writeValue(earliestPubDateMs)
        parcel.writeString(title)
        parcel.writeString(listennotesUrl)
        parcel.writeValue(totalEpisodes)
        parcel.writeValue(isClaimed)
        parcel.writeValue(nextEpisodePubDate)
        parcel.writeString(rss)
        parcel.writeValue(lastestPubDateMs)
        parcel.writeParcelable(lookingFor, flags)
        parcel.writeStringList(genres)
        parcel.writeParcelable(extra, flags)
        parcel.writeString(publisher)
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeTypedList(episodes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Podcasts> {
        override fun createFromParcel(parcel: Parcel): Podcasts {
            return Podcasts(parcel)
        }

        override fun newArray(size: Int): Array<Podcasts?> {
            return arrayOfNulls(size)
        }
    }

    fun getTotalEpisodes(): String? {
        return "$totalEpisodes Bölüm"
    }

    fun getEmailVisibility(): Boolean? {
        return !email.isNullOrEmpty()
    }

    fun getPodcastLanguage(): String? {
        return "Yayın Dili : $language"
    }

    fun getPodcastPublisher(): String? {
        return "Yayıncı : $publisher"
    }

    fun getPubDateMs(): String {
        return """İlk Yayın Tarihi : ${getDateTime(earliestPubDateMs ?: 0)}""".trimMargin()
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