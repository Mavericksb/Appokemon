package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Ability(
    @Json( name = "ability")
    val ability: AbilityX?,
    @Json( name = "is_hidden")
    val isHidden: Boolean?,
    @Json( name = "slot")
    val slot: Int?
)