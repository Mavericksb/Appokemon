package com.rob.gab.appokemon.ui.home.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.model.PokemonModel
import com.rob.gab.appokemon.ui.home.HomeFragment.Companion.TAG
import kotlinx.android.synthetic.main.home_list_item.view.*
import kotlinx.android.synthetic.main.home_load_state_footer_view_item.view.*


val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<PokemonModel>() {
    override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean =
        // Pokemon ID serves as unique ID
        oldItem.id == newItem.id
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean =
        oldItem == newItem
}


class PokemonListAdapter(private val listener: ((Int) -> Unit)) : PagingDataAdapter<PokemonModel, PokemonListAdapter.ViewHolder>(POKEMON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(pokemonModel: PokemonModel?) {
            with(itemView) {
                pokemonName.text = pokemonModel?.name
                Log.d(TAG, "Start loading image: ${pokemonModel?.imageUrl}")
                Glide
                    .with(context)
                    .load(pokemonModel?.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.pokeball)
                    .into(pokemonImage);

                setOnClickListener {
                    pokemonModel?.id?.let { listener.invoke(it) }
                }
            }
        }
    }


}
