package com.rob.gab.appokemon.repository

import androidx.paging.PagingData
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonStream(offset: Int?, pageLimit: Int): Flow<PagingData<EntityPokemon>>

// # Alternative method, using a flow
//    suspend fun getPokemonDetailsResource(id: Int): Flow<Resource<*>>

    suspend fun getPokemonDetails(id: Int): PokemonDetailsModel?

    suspend fun getPokemonByName(name: String): PokemonDetailsModel?
}