package com.rob.gab.appokemon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.domain.model.PokemonModel
import com.rob.gab.appokemon.ui.home.adapter.PokemonListAdapter
import com.rob.gab.appokemon.ui.widget.FloatingToastDialog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private var floatingToast: FloatingToastDialog? = null
    private lateinit var mAdapter: PokemonListAdapter

    private val viewModel: HomeViewModel by viewModel()

    companion object {
        const val TAG = "PokemonListFragment"
        const val FLOATING_TOAST_TIMEOUT = 4000L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        observeState()
    }


    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    //Actually we just have a single useful State
                    is HomeState.Success -> submitToAdapter(it.data)
                }
            }
        }
    }

    private fun submitToAdapter(it: PagingData<PokemonModel>) {
        swipeToRefreshLayout.isRefreshing = false
        viewModel.viewModelScope.launch {
            mAdapter.submitData(it)
        }
    }

    override fun onPause() {
        super.onPause()
        floatingToast?.dismiss()
    }

    private fun initComponents() {
        val navController = Navigation.findNavController(requireView())

        val layoutManager = GridLayoutManager(requireContext(), 2)
        pokemonRecyclerView.layoutManager = layoutManager

        //Init the adapter
        mAdapter = PokemonListAdapter { id ->
            //Route to detail fragment when click on a Pokemon
            navController.navigate(HomeFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id))
        }

        mAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            pokemonRecyclerView?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            progress_bar?.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            retry_button?.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error?: loadState.refresh as? LoadState.Error
            errorState?.let {
                 context?.let { floatingToast = FloatingToastDialog(requireContext(),
                    "An error occurred. Please retry again.",
                    FloatingToastDialog.FloatingToastType.Error).timer(FLOATING_TOAST_TIMEOUT)
                floatingToast?.slideDown()?.show() }
            }
        }

        pokemonRecyclerView.adapter = mAdapter

        retry_button.setOnClickListener { mAdapter.retry() }
        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.userIntent.offer(HomeIntent.FetchPokemons)
        }
    }

}
