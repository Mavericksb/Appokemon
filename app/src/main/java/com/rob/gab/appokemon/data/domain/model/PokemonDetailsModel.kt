package com.rob.gab.appokemon.data.domain.model

import android.os.Parcelable
import com.rob.gab.appokemon.Constants.IMAGE_URL
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonDetailsModel(
    val id: Int,
    var name: String? = null,
    var height: Double? = null,
    var weight: Double? = null,
    var types: List<Type>? = null,
    var stats: List<Stats>? = null
) : Parcelable {

    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"

    @Parcelize
    data class Type(
        var name: String? = null,
        var url: String? = null
    ) : Parcelable

    @Parcelize
    data class Stats(
        var base: Int? = null,
        var effort: Int? = null,
        var name: String? = null,
        var url: String? = null
    ) : Parcelable

}