package com.rob.gab.appokemon.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rob.gab.appokemon.model.PokemonModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonStream(pageLimit: Int): Flow<PagingData<PokemonModel>>
}