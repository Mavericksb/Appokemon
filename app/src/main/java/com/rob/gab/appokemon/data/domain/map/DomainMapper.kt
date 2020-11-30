package com.rob.gab.appokemon.data.domain.map

import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.dao.EntityPokemonDetails
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.data.domain.model.PokemonModel
import com.rob.gab.appokemon.data.network.dto.PokemonDetailsResponse
import com.rob.gab.appokemon.data.network.dto.PokemonResponse
import com.rob.gab.appokemon.utils.Utils

fun pokemonEntityToDomain(entity: EntityPokemon): PokemonModel {
    return PokemonModel(entity.name, entity.id)
}

fun mapDetailsEntityToDomain(entityDetails: EntityPokemonDetails): PokemonDetailsModel {
    return PokemonDetailsModel(
        id = entityDetails.id,
        name = entityDetails.name,
        height = entityDetails.height,
        weight = entityDetails.weight,
        types = entityDetails.types?.map { PokemonDetailsModel.Type(it.name, it.url) },
        stats = entityDetails.stats?.map { PokemonDetailsModel.Stats(it.base, it.effort, it.name, it.url) }
    )
}

fun mapDetailsResponseToDomain(pokemonResponse: PokemonDetailsResponse): PokemonDetailsModel {
    return PokemonDetailsModel(pokemonResponse.id!!).apply {
        name = pokemonResponse.name?.capitalize()
        height = pokemonResponse.height?.let { it / 10.0 } //The height of this Pokémon in decimetres.
        weight = pokemonResponse.weight?.let { it / 10.0 } //The weight of this Pokémon in hectograms.
        types = pokemonResponse.types?.map {
            PokemonDetailsModel.Type().apply {
                name = it.type?.name
                url = it.type?.url
            }
        }
        stats = pokemonResponse.stats?.map {
            PokemonDetailsModel.Stats().apply {
                base = it.baseStat
                effort = it.effort
                name = it.stat?.name
                url = it.stat?.url
            }
        }?.sortedWith(Comparator { o1, o2 ->
            val length1 = o1.name?.length ?: 0
            val length2 = o2.name?.length ?: 0
            return@Comparator -length1.compareTo(length2)
        })
    }
}

fun mapPokemonResponseToDomain(pokemonResponse: PokemonResponse?): List<PokemonModel> {
    return arrayListOf<PokemonModel>().apply {
        pokemonResponse?.results?.forEach { result ->
            if (result.name != null && result.url != null) {
                add(PokemonModel(result.name.capitalize(), Utils.urlToId(result.url)))
            }
        }
    }
}