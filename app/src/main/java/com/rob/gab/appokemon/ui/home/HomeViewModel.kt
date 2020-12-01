package com.rob.gab.appokemon.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.PokemonApplication.Companion.applicationContext
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.map.pokemonEntityToDomain
import com.rob.gab.appokemon.data.domain.model.PokemonModel
import com.rob.gab.appokemon.repository.PokemonRepository
import io.uniflow.androidx.flow.AndroidDataFlow
import io.uniflow.core.flow.data.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class HomeViewModel(private val repository: PokemonRepository) : AndroidDataFlow() {

    private var pagingData: Flow<PagingData<PokemonModel>>? = null
    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val searchString: MutableLiveData<String>? = null
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState>
        get() = _state

    init {
        viewModelScope.launch {
            handleIntent()
        }
    }

    private suspend fun handleIntent() {
        userIntent.consumeAsFlow().collectLatest {
            when (it) {
                is HomeIntent.GetPokemons -> {
                    viewModelScope.launch { getPokemons(it.offset) }
                }
                is HomeIntent.Search -> {
                    searchString?.value = it.name
                    applyFilter(it.name)
                }
            }
        }
    }

    suspend fun applyFilter(value: String?){
        pagingData?.map { model ->
            model.filter { it.name.startsWith(value?: "", ignoreCase = true)  }
        }?.cachedIn(viewModelScope)?.collect { _state.value = HomeState.Success(list = it) }
    }

    suspend fun getPokemons(offset: Int?) {
        pagingData = repository.getPokemonStream(offset, PAGE_LIMIT)
            .map { entity ->
                    entity.map { pokemonEntityToDomain(it) }
            }
            .cachedIn(viewModelScope)
        applyFilter(searchString?.value)
    }
}