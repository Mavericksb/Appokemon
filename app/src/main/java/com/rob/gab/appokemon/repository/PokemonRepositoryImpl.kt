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

    @ExperimentalPagingApi
    override fun getPokemonStream(offset: Int?, pageLimit: Int): Flow<PagingData<EntityPokemon>> {
        val pagingSourceFactory = { database.pokemonDao().getPokemons() }

        return Pager(
            config = PagingConfig(
                initialLoadSize = pageLimit*3,
                pageSize = pageLimit,
                enablePlaceholders = true
            ),
            initialKey = offset,
            remoteMediator = pokemonsRemoteMediator,
            pagingSourceFactory = { pagingSourceFactory() }).flow
    }

    override suspend fun getPokemonDetails(id: Int): PokemonDetailsModel? {
        var pokemonDetails: PokemonDetailsModel? = null
        //catch any network error
        try {
            val response = apiService.getPokemonDetails(id)
            //Network response è ok, mantengo aggiornato il database
            response?.let {
                //Todo: mapper diretto
                pokemonDetails = mapDetailsResponseToDomain(it)
                database.pokemonDao().insertDetails(mapDetailsDomainToEntity(pokemonDetails!!))
            }
        } catch (e: Exception){
            //If network has any error, try to retrieve pokemon from DB
            pokemonDetails = mapDetailsEntityToDomain(database.pokemonDao().getPokemonDetails(id))
        }
        return pokemonDetails
    }

    // # Metodo alternativo, utilizzano un flow ed una Response::class come wrapper per aver accesso anche ai metadata della chiamata
    // Ho evitato l'uso perché del tutto inutile con questa Api
//    override suspend fun getPokemonDetailsResource(id: Int): Flow<Resource<*>> {
//        return flow {
//
//            emit(Resource.loading())
//
//            val response = apiService.getPokemonDetailsResponse(id)
//
//            if (response?.isSuccessful == true) {
//                val pokemonResponse = response.body()
//                pokemonResponse?.let {
//                    database.pokemonDao().insertDetails(mapDetailsDomainToEntity(mapDetailsResponseToDomain(it)))
//                }
//                try {
//                    val result = database.pokemonDao().getPokemonDetails(id)
//                    emit(Resource.success(mapDetailsEntityToDomain(result)))
//                } catch (e: Exception) {
//                    emit(Resource.error(applicationContext.getString(R.string.generic_network_error_message)))
//                }
//            } else {
//                emit(Resource.error(applicationContext.getString(R.string.generic_network_error_message)))
//            }
//
//        }
//    }
}