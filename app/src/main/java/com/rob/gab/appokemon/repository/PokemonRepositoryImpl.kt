package com.rob.gab.appokemon.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rob.gab.appokemon.model.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.model.PokemonModel
import com.rob.gab.appokemon.remote.ApiService
import com.rob.gab.appokemon.remote.PokemonPagingSource
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(
    private val apiService: ApiService,
    private val pokemonPagingSource: PokemonPagingSource
): PokemonRepository {
    override fun getPokemonStream(pageLimit: Int): Flow<PagingData<PokemonModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = pageLimit,
                pageSize = pageLimit,
                enablePlaceholders =  false
        ),
            pagingSourceFactory = { pokemonPagingSource }).flow
    }

}