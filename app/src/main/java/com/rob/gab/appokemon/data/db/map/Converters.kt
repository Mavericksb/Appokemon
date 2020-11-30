package com.rob.gab.appokemon.data.db.map

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.rob.gab.appokemon.data.db.dao.EntityPokemonDetails.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private var moshi: Moshi
    init {
        moshi = Moshi.Builder().build()
    }

    @TypeConverter
    fun fromTypeToJson(list: List<Type>?): String? {
        val type = Types.newParameterizedType(List::class.java, Type::class.java)
        val adapter = moshi.adapter<List<Type>>(type)
        return adapter.toJson(list?: emptyList())
    }

    @TypeConverter
    fun fromJsonToType(value: String?): List<Type>? {
        val type = Types.newParameterizedType(List::class.java, Type::class.java)
        val adapter = moshi.adapter<List<Type>>(type)
        return adapter.fromJson(value?: "")
    }

    @TypeConverter
    fun fromStatsToJson(list: List<Stats>?): String? {
        val type = Types.newParameterizedType(List::class.java, Stats::class.java)
        val adapter = moshi.adapter<List<Stats>>(type)
        return adapter.toJson(list?: emptyList())
    }

    @TypeConverter
    fun fromJsonToStats(value: String?): List<Stats>? {
        val type = Types.newParameterizedType(List::class.java, Stats::class.java)
        val adapter = moshi.adapter<List<Stats>>(type)
        return adapter.fromJson(value?: "")
    }

}