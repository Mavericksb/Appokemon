package com.rob.gab.appokemon.ui.details

sealed class DetailIntent {
    class GetPokemonDetails(val id: Int) : DetailIntent()
}