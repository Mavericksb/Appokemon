package com.rob.gab.appokemon.model.map

import com.rob.gab.appokemon.model.PokemonModel
import com.rob.gab.appokemon.remote.dto.PokemonResponse
import com.rob.gab.appokemon.utils.Utils.urlToId

fun mapPokemon(pokemonResponse: PokemonResponse?): List<PokemonModel> {
    return arrayListOf<PokemonModel>().apply {
        pokemonResponse?.results?.forEach { result ->
            if (result.name != null && result.url != null) {
                add(PokemonModel(result.name.capitalize(), urlToId(result.url)))
            }
        }
    }
}