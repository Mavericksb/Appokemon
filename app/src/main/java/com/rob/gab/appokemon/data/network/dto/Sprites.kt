package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Sprites(
    @Json( name = "back_default")
    val backDefault: String?,
    @Json( name = "back_female")
    val backFemale: Any?,
    @Json( name = "back_shiny")
    val backShiny: String?,
    @Json( name = "back_shiny_female")
    val backShinyFemale: Any?,
    @Json( name = "front_default")
    val frontDefault: String?,
    @Json( name = "front_female")
    val frontFemale: Any?,
    @Json( name = "front_shiny")
    val frontShiny: String?,
    @Json( name = "front_shiny_female")
    val frontShinyFemale: Any?
)