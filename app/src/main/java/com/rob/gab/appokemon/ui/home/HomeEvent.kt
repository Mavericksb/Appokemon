package com.rob.gab.appokemon.ui.home

import androidx.paging.PagingData
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.data.domain.model.PokemonModel
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState

sealed class HomeEvent: UIEvent() {
    object Idle : HomeEvent()
    object Loading : HomeEvent()
    data class Search(val pokemon: PokemonDetailsModel? =null) : UIEvent() //, val detail: PokemonDetailsModel?=null) : HomeState()


    object Failed : HomeEvent()
}