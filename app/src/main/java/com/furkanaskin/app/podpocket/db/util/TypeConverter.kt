package com.furkanaskin.app.podpocket.db.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Furkan on 2019-07-03
 */

class TypeConverter {

    @TypeConverter
    fun stringToStringList(data: String?): List<String> {
        if (data == null) {
            return listOf()
        }

        val listType = object : TypeToken<List<String>>() {
        }.type

        return Gson().fromJson<List<String>>(data, listType)
    }

    @TypeConverter
    fun stringListToString(someObjects: List<String>): String {
        return Gson().toJson(someObjects)
    }
}