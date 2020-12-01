package com.rob.gab.appokemon.ui.home

import androidx.paging.PagingData
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.data.domain.model.PokemonModel
import com.rob.gab.appokemon.utils.Event

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val list: PagingData<PokemonModel>?=null,  val searchEvent: Event<PokemonDetailsModel?>?=null) : HomeState()


    class Failed(val message: String?) : HomeState()
}