package com.rob.gab.appokemon.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
//    @JsonProperty("count")
    val count: Int?,
//    @JsonProperty("next")
    val next: String?,
//    @JsonProperty("previous")
    val previous: Any?,
//    @JsonProperty("results")
    val results: List<Result>?
)