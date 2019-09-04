package com.furkanaskin.app.podpocket.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furkanaskin.app.podpocket.model.Post
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import java.util.Locale

/**
 * Created by Furkan on 2019-07-03
 */

@Entity(tableName = "Posts")
class PostEntity(
    var post: String? = null,
    @PrimaryKey
    var postId: String,
    var pubDate: String? = null,
    var region: String? = null,
    var userName: String? = null,
    var userUniqueId: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(post)
        parcel.writeString(postId)
        parcel.writeString(pubDate)
        parcel.writeString(region)
        parcel.writeString(userName)
        parcel.writeString(userUniqueId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostEntity> {
        override fun createFromParcel(parcel: Parcel): PostEntity {
            return PostEntity(parcel)
        }

        override fun newArray(size: Int): Array<PostEntity?> {
            return arrayOfNulls(size)
        }
    }

    constructor(postItem: Post) : this(
        post = postItem.post,
        postId = postItem.postId ?: "",
        pubDate = postItem.pubDate,
        region = postItem.region,
        userName = postItem.userName,
        userUniqueId = postItem.userUniqueId
    )

    fun getTimeAgoString(): String? {
        val localeBylanguageTag = Locale.forLanguageTag("tr")
        val messages = TimeAgoMessages.Builder().withLocale(localeBylanguageTag).build()
        return pubDate?.toLong()?.let { TimeAgo.using(it, messages) }
    }
}
