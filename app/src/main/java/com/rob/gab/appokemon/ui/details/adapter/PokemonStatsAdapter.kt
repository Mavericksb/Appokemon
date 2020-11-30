package com.rob.gab.appokemon.ui.details.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import kotlinx.android.synthetic.main.details_stats_item.view.*


class PokemonStatsAdapter() : RecyclerView.Adapter<PokemonStatsAdapter.ViewHolder>() {
    private val pokemonStats = mutableListOf<PokemonDetailsModel.Stats>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.details_stats_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = pokemonStats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindItem(pokemonStats[position])

    fun switchData(data: List<PokemonDetailsModel.Stats>?) {
        pokemonStats.clear()
        data?.let {
            pokemonStats.addAll(data)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(statsModel: PokemonDetailsModel.Stats) {

            with(itemView) {
                statsLabel.text = context.getString(
                    R.string.stats_label, statsModel.name,
                    statsModel.base, statsModel.effort
                )

                setOnClickListener {
                    // TODO:AV
                    Toast.makeText(context, "TODO: open stats", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}