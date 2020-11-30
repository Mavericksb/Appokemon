package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Stat(
    @Json( name = "base_stat")
    val baseStat: Int?,
    @Json( name = "effort")
    val effort: Int?,
    @Json( name = "stat")
    val stat: StatX?
)