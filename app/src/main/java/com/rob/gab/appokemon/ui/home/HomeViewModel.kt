package com.rob.gab.appokemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.PokemonApplication
import com.rob.gab.appokemon.PokemonApplication.Companion.applicationContext
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.map.pokemonEntityToDomain
import com.rob.gab.appokemon.repository.PokemonRepository
import com.rob.gab.appokemon.ui.details.DetailState
import io.uniflow.androidx.flow.AndroidDataFlow
import io.uniflow.core.flow.data.Event
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class HomeViewModel(private val repository: PokemonRepository): AndroidDataFlow() {

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
                is HomeIntent.GetPokemons -> {
                    viewModelScope.launch { getPokemons(it.offset) }
                }
                is HomeIntent.GetPokemonByName -> {
                    viewModelScope.launch { getPokemonByName(it.name) }
                }
            }
        }
    }

    private suspend fun getPokemonByName(name: String) {
        _state.value = HomeState.Loading
        try {
            val response = repository.getPokemonByName(name.toLowerCase(Locale.getDefault()))
            _state.value = HomeState.Success(searchEvent = Event(response))
        } catch (e: Exception){
            //If both network and DB fail to get Pokemon details, show error
            _state.value = HomeState.Failed(applicationContext.getString(R.string.no_pokemon_found))
        }

    }

    suspend fun getPokemons(offset: Int?) {
            repository.getPokemonStream(offset, PAGE_LIMIT)
                .cachedIn(viewModelScope).map {
                    it.map {
                        pokemonEntityToDomain(it)
                    }
                }
                .collectLatest {
                    _state.value = HomeState.Success(list = it)
                }
    }
}