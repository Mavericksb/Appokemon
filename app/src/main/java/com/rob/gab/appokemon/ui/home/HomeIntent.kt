package com.rob.gab.appokemon.ui.home

sealed class HomeIntent {
    class GetPokemons(val offset: Int? = null) : HomeIntent()
    class GetPokemonByName(val name: String) : HomeIntent()
}