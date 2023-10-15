package com.example.ul_todo_android_app.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }
}