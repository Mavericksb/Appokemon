package com.rob.gab.appokemon.data.domain.model

import com.rob.gab.appokemon.Constants.IMAGE_URL

class PokemonModel(val name: String, val id: Int) {
    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PokemonModel

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id
        return result
    }
}