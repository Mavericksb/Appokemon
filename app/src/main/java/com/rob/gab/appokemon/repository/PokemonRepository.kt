package com.rob.gab.appokemon.repository

import androidx.paging.PagingData
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonStream(pageLimit: Int): Flow<PagingData<EntityPokemon>>

    suspend fun getPokemonDetails(id: Int): PokemonDetailsModel?
}