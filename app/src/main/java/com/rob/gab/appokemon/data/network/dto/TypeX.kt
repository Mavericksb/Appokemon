package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TypeX(
    @Json( name = "name")
    val name: String?,
    @Json( name = "url")
    val url: String?
)