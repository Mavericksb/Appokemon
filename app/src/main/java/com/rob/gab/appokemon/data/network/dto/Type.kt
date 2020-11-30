package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Type(
    @Json( name = "slot")
    val slot: Int?,
    @Json( name = "type")
    val type: TypeX?
)