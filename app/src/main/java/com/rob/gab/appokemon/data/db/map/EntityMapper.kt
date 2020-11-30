package com.rob.gab.appokemon.data.db.map

import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.dao.EntityPokemonDetails
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.data.network.dto.PokemonResponse
import com.rob.gab.appokemon.utils.Utils


fun mapDetailsDomainToEntity(pokemonDetailsModel: PokemonDetailsModel): EntityPokemonDetails {
    return EntityPokemonDetails(
        id = pokemonDetailsModel.id,
        name = pokemonDetailsModel.name,
        height = pokemonDetailsModel.height,
        weight = pokemonDetailsModel.weight,
        types = pokemonDetailsModel.types?.map { EntityPokemonDetails.Type(it.name, it.url) },
        stats = pokemonDetailsModel.stats?.map { EntityPokemonDetails.Stats(it.base, it.effort, it.name, it.url) }
    )
}

fun mapResponseToEntity(pokemonResponse: PokemonResponse?): List<EntityPokemon> {
    return arrayListOf<EntityPokemon>().apply {
        pokemonResponse?.results?.forEach { result ->
            if (result.name != null && result.url != null) {
                add(EntityPokemon(Utils.urlToId(result.url), result.name.capitalize() ))
            }
        }
    }
}