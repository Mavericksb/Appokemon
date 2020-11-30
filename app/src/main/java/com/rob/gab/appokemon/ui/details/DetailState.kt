package com.rob.gab.appokemon.ui.details

import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel


sealed class DetailState{
    object Idle : DetailState()
    object Loading : DetailState()
    data class Success(val data: PokemonDetailsModel?) : DetailState()
    object Failed : DetailState()
}

