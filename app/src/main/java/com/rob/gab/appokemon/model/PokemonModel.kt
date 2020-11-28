package com.rob.gab.appokemon.model

import com.rob.gab.appokemon.model.Constants.IMAGE_URL

class PokemonModel(val name: String, val id: Int) {
    val imageUrl: String
        get() = "$IMAGE_URL/$id.png"
}