package com.rob.gab.appokemon.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rob.gab.appokemon.model.PokemonModel
import com.rob.gab.appokemon.remote.dto.PokemonResponse
import com.rob.gab.appokemon.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

class GetPokemons(private val pokemonRepository: PokemonRepository) {
        operator fun invoke(pageLimit: Int) : Flow<PagingData<PokemonModel>> {
            return pokemonRepository.getPokemonStream(pageLimit)
        }
}