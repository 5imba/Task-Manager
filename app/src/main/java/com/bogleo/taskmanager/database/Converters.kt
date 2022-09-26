package com.bogleo.taskmanager.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listStringToJsonString(item: List<String>): String = Gson().toJson(item)

    @TypeConverter
    fun jsonStringToListString(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}