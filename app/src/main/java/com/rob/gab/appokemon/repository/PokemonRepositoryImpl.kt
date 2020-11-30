package com.rob.gab.appokemon.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rob.gab.appokemon.data.db.PokemonDatabase
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.map.mapDetailsDomainToEntity
import com.rob.gab.appokemon.data.domain.map.mapDetailsEntityToDomain
import com.rob.gab.appokemon.data.domain.map.mapDetailsResponseToDomain
import com.rob.gab.appokemon.data.network.ApiService
import com.rob.gab.appokemon.data.network.PokemonsRemoteMediator
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl @ExperimentalPagingApi constructor(
    private val apiService: ApiService,
    private val pokemonsRemoteMediator: PokemonsRemoteMediator,
    private val database: PokemonDatabase
) : PokemonRepository {


    override fun getPokemonStream(pageLimit: Int): Flow<PagingData<EntityPokemon>> {
        val pagingSourceFactory = { database.pokemonDao().getPokemons() }

        return Pager(
            config = PagingConfig(
                initialLoadSize = pageLimit,
                pageSize = pageLimit,
                enablePlaceholders = false
            ),
            remoteMediator = pokemonsRemoteMediator,
            pagingSourceFactory = { pagingSourceFactory() }).flow
    }

    override suspend fun getPokemonDetails(id: Int): PokemonDetailsModel? {
        var pokemonDetails: PokemonDetailsModel? = null
        try {
            val response = apiService.getPokemonDetails(id)
            response?.let {
                pokemonDetails = mapDetailsResponseToDomain(it)
                database.pokemonDao().insertDetails(mapDetailsDomainToEntity(pokemonDetails!!))
            }
        } catch (e: Exception){
            pokemonDetails = mapDetailsEntityToDomain(database.pokemonDao().getPokemonDetails(id))
        }
        return pokemonDetails
    }

}