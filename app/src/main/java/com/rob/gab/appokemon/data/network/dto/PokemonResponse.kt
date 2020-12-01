package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    val count: Int?,
    val next: String?,
    val previous: Any?,
    val results: List<Result>?
)