package com.rob.gab.appokemon.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.data.domain.model.PokemonDetailsModel
import com.rob.gab.appokemon.ui.details.adapter.PokemonStatsAdapter
import com.rob.gab.appokemon.ui.widget.FloatingToastDialog
import com.rob.gab.appokemon.utils.Utils
import com.rob.gab.appokemon.utils.Utils.hideLoading
import com.rob.gab.appokemon.utils.Utils.showLoading
import com.vit.ant.pokemon.view.adapter.PokemonTypeAdapter
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class DetailFragment : Fragment() {

    private lateinit var mTypeAdapter: PokemonTypeAdapter
    private lateinit var mStatsAdapter: PokemonStatsAdapter
    private var mPokemonId = 0

    private val detailViewModel: DetailViewModel by viewModel()

    companion object {
        const val TAG = "PokemonDetailsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPokemonId = DetailFragmentArgs.fromBundle(requireArguments()).id
        detailViewModel.userIntent.offer(DetailIntent.GetPokemonDetails(mPokemonId))

        initComponents()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            detailViewModel.state.collectLatest {
                when (it) {
                    is DetailState.Loading -> {
                        showLoading(WeakReference(requireActivity()))
                    }
                    is DetailState.Success -> {
                        hideLoading(WeakReference(requireActivity()))
                        fillPokemonDetails(it.data)
                    }
                    is DetailState.Failed -> {
                        hideLoading(WeakReference(requireActivity()))
                        FloatingToastDialog(requireContext(), "Impossibile contattare il server", FloatingToastDialog.FloatingToastType.Error)
                            .slideDown().show()
                    }
                }
            }
        }
    }

    private fun initComponents() {
        typesRecyclerView.layoutManager = StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL)
        mTypeAdapter = PokemonTypeAdapter()
        typesRecyclerView.adapter = mTypeAdapter

        statsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL)
        mStatsAdapter = PokemonStatsAdapter()
        statsRecyclerView.adapter = mStatsAdapter
    }

    private fun fillPokemonDetails(detailsModel: PokemonDetailsModel?) {
        Glide
            .with(requireContext())
            .load(detailsModel?.imageUrl)
            .placeholder(R.drawable.pokeball)
            .error(R.drawable.pokeball)
            .into(pokemonImage)
        pokemonName.text = detailsModel?.name
        val heightString = Utils.oneDecimalFormater.format(detailsModel?.height ?: 0.0)
        pokemonHeight.text = getString(R.string.pokemon_height, heightString)
        val weightString = Utils.oneDecimalFormater.format(detailsModel?.weight ?: 0.0)
        pokemonWeight.text = getString(R.string.pokemon_weight, weightString)
        mTypeAdapter.switchData(detailsModel?.types)
        mStatsAdapter.switchData(detailsModel?.stats)
    }

}
