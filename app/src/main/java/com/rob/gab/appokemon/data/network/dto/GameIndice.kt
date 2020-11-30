package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GameIndice(
    @Json( name = "game_index")
    val gameIndex: Int?,
    @Json( name = "version")
    val version: Version?
)