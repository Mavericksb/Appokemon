package com.rob.gab.appokemon.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rob.gab.appokemon.PokemonApplication.Companion.applicationContext
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.data.network.Resource
import com.rob.gab.appokemon.repository.PokemonRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.lang.Exception

typealias Handler<State, Action, Mutation> = (State, Action) -> Flow<Mutation>

class DetailViewModel(private val repository: PokemonRepository): ViewModel() {

    val userIntent = Channel<DetailIntent>(Channel.UNLIMITED)


    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state: StateFlow<DetailState>
        get() = _state

    init {
        viewModelScope.launch {
            handleIntent()
        }
    }

    private suspend fun handleIntent() {
        userIntent.consumeAsFlow().collect {
            when (it) {
                is DetailIntent.GetPokemonDetails -> {
                    viewModelScope.launch { fetchPokemonDetails(it.id) }
                }
            }
        }
    }

    suspend fun fetchPokemonDetails(id: Int) {
        _state.value = DetailState.Loading
        try {
            val response = repository.getPokemonDetails(id)
            _state.value = DetailState.Success(response)
        } catch (e: Exception){
            //If both network and DB fail to get Pokemon details, show error
            _state.value = DetailState.Failed(applicationContext.getString(R.string.generic_network_error_message))
        }
    }

    // # Alternative method, using a flow
//    suspend fun fetchPokemonDetails(id: Int) {
//            val response = repository.getPokemonDetailsResource(id)
//            response.collect {
//                _state.value = when(it.status){
//                    Resource.Status.LOADING -> { DetailState.Loading }
//                    Resource.Status.ERROR -> {DetailState.Failed(it.errorMessage)}
//                    Resource.Status.SUCCESS -> {DetailState.Success(it.data as? PokemonDetailsModel)}
//                }
//            }
//    }
}

