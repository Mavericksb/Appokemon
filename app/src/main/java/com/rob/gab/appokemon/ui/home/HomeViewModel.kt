package com.rob.gab.appokemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.data.domain.map.pokemonEntityToDomain
import com.rob.gab.appokemon.repository.PokemonRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PokemonRepository): ViewModel() {

    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState>
        get() = _state

    init {
        viewModelScope.launch {
            handleIntent()
        }
    }

    private suspend fun handleIntent() {
        userIntent.consumeAsFlow().collect {
            when (it) {
                is HomeIntent.FetchPokemons -> {
                    viewModelScope.launch { getPokemons() }
                }
            }
        }
    }

    suspend fun getPokemons() {
        repository.getPokemonStream(PAGE_LIMIT)
            .cachedIn(viewModelScope).map { it.map {
                pokemonEntityToDomain(it) } }
            .collectLatest {
                _state.value = HomeState.Success(it)
            }
    }
}