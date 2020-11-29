package com.rob.gab.appokemon.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rob.gab.appokemon.data.db.PokemonDatabase
import com.rob.gab.appokemon.domain.model.PokemonModel
import com.rob.gab.appokemon.data.remote.ApiService
import com.rob.gab.appokemon.data.remote.PokemonPagingSource
import com.rob.gab.appokemon.data.remote.PokemonsRemoteMediator
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl @ExperimentalPagingApi constructor(
    private val apiService: ApiService,
    private val pokemonsRemoteMediator: PokemonsRemoteMediator,
//    private val pokemonPagingSource: PokemonPagingSource
    private val database: PokemonDatabase
): PokemonRepository {



    override fun getPokemonStream(pageLimit: Int): Flow<PagingData<PokemonModel>> {

        val pagingSourceFactory =  database.pokemonDao().getPokemons(0, pageLimit)

        return Pager(
            config = PagingConfig(
                initialLoadSize = pageLimit,
                pageSize = pageLimit,
                enablePlaceholders =  false
        ),
            remoteMediator = pokemonsRemoteMediator,
            pagingSourceFactory = { pagingSourceFactory }).flow
    }

}