package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val name: String?,
    val url: String?
)