package com.rob.gab.appokemon.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.domain.model.PokemonModel
import com.rob.gab.appokemon.usecase.GetPokemons
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val getPokemons: GetPokemons) : AndroidDataFlow() {

    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState>
        get() = _state

    init {
        viewModelScope.launch {
            handleIntent()
        }
        userIntent.offer(HomeIntent.FetchPokemons)
    }

    private suspend fun handleIntent() {
        userIntent.consumeAsFlow().collect {
            when (it) {
                is HomeIntent.FetchPokemons -> {
                    viewModelScope.launch { fetchPokemons() }
                }
            }
        }
    }

    suspend fun fetchPokemons() {
        getPokemons(PAGE_LIMIT)
            .cachedIn(viewModelScope)
            .collect { _state.value = HomeState.Success(it) }
    }
}