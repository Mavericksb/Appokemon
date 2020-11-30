package com.vit.ant.pokemon.view.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.utils.Utils.getColorByType
import kotlinx.android.synthetic.main.details_type_item.view.*


class PokemonTypeAdapter() : RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {
    private val pokemonTypes = mutableListOf<PokemonDetailsModel.Type>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.details_type_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = pokemonTypes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(pokemonTypes[position])

    fun switchData(data: List<PokemonDetailsModel.Type>?) {
        pokemonTypes.clear()
        data?.let {
            pokemonTypes.addAll(data)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(typeModel: PokemonDetailsModel.Type) {

            with(itemView) {
                typeLabel.text = typeModel.name

                val color = getColorByType(typeModel.name)
                DrawableCompat.setTint(typeLabel.background, color)

                setOnClickListener {
                    // TODO:AV
                    Toast.makeText(context, "TODO: open type", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}