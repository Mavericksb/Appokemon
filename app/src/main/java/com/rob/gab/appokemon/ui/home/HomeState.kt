package com.rob.gab.appokemon.ui.home

import androidx.paging.PagingData
import com.rob.gab.appokemon.domain.model.PokemonModel


sealed class HomeState{
    object Idle : HomeState()
    data class Success(val data: PagingData<PokemonModel>) : HomeState()
    object Failed : HomeState()
}