package com.furkanaskin.app.podpocket.service.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Search(

    @field:SerializedName("took")
    val took: Double? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("next_offset")
    val nextOffset: Int? = null,

    @field:SerializedName("results")
    val results: List<ResultsItem?>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(ResultsItem::class.java.classLoader) as? List<ResultsItem>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(took)
        parcel.writeValue(total)
        parcel.writeValue(count)
        parcel.writeValue(nextOffset)
        parcel.writeValue(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
    }
}