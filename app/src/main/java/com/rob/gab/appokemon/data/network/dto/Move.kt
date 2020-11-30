package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Move(
    @Json( name = "move")
    val move: MoveX?,
    @Json( name = "version_group_details")
    val versionGroupDetails: List<VersionGroupDetail>?
)