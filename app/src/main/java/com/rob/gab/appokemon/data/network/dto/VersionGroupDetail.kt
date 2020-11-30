package com.rob.gab.appokemon.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class VersionGroupDetail(
    @Json( name = "level_learned_at")
    val levelLearnedAt: Int?,
    @Json( name = "move_learn_method")
    val moveLearnMethod: MoveLearnMethod?,
    @Json( name = "version_group")
    val versionGroup: VersionGroup?
)