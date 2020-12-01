package com.rob.gab.appokemon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.ui.home.adapter.PokemonListAdapter
import com.rob.gab.appokemon.ui.widget.FloatingToastDialog
import com.rob.gab.appokemon.utils.Utils
import com.rob.gab.appokemon.utils.Utils.hideLoading
import com.rob.gab.appokemon.utils.Utils.showLoading
import io.uniflow.androidx.flow.onEvents
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var navController: NavController
    private var mSearchId: Int? = null
    private var floatingToast: FloatingToastDialog? = null
    private lateinit var mAdapter: PokemonListAdapter

    private val viewModel: HomeViewModel by viewModel()

    companion object {
        const val TAG = "PokemonListFragment"
        const val FLOATING_TOAST_TIMEOUT = 4000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Invio un Intention per avviare il fetching dei dati
        // Il PagingAdapter 3.0 gestisce retry e refresh con metodi dedicati

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userIntent.offer(HomeIntent.GetPokemons())
        initComponents()
        initAdapterStateListener()
        observeState()
    }


    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is HomeState.Success -> {
                        hideLoading(WeakReference(requireActivity()))
                        it.list?.let { pagingData  -> mAdapter.submitData(pagingData) }
                        it.searchEvent?.let { event ->
                            event.take()?.let { pokemon -> navController.navigate(HomeFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id = -1, pokemon = pokemon)) }
                            swipeToRefreshLayout?.isRefreshing = false
                            searchBtn.isEnabled=true
                        }
                    }
                    is HomeState.Loading -> showLoading(WeakReference(requireActivity()))
                    is HomeState.Failed -> {
                        it.message?.let { FloatingToastDialog(requireContext(), it, FloatingToastDialog.FloatingToastType.Alert) }
                        hideLoading(WeakReference(requireActivity()))
                    }
                }
            }
        }
    }


    private fun initComponents() {
        navController = Navigation.findNavController(requireView())

        mAdapter = PokemonListAdapter { id ->
            navController.navigate(HomeFragmentDirections.actionPokemonListFragmentToPokemonDetailsFragment(id, null))
        }

        with(pokemonRecyclerView) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }

        retry_button.setOnClickListener { mAdapter.retry() }

        swipeToRefreshLayout.setOnRefreshListener {
            if (mAdapter.itemCount <= 0) {
                mAdapter.retry()
                swipeToRefreshLayout.isRefreshing = false
            } else {
                mAdapter.refresh()
            }
        }
    }

    private fun initAdapterStateListener() {
        mAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
//            pokemonRecyclerView?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            progress_bar?.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            alertMessage?.visibility = if (loadState.refresh is LoadState.Error
                || loadState.append is LoadState.Error
            ) VISIBLE else GONE

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: if (mAdapter.itemCount <= 0) loadState.refresh as? LoadState.Error else null

            errorState?.let {
                if (context != null && floatingToast?.isShowing != true) {
                    floatingToast = FloatingToastDialog(
                        requireContext(),
                        "Assicurati di essere connesso ad internet",
                        FloatingToastDialog.FloatingToastType.Error
                    ).timer(FLOATING_TOAST_TIMEOUT).also { it.slideDown().show() }
                }
            }
        }

        searchBtn.setOnClickListener {
            val name = inputName.text.toString()
            if (name.isNotEmpty()) {
                searchPokemon(name)
                searchBtn.isEnabled=false
            }
        }
    }

    private fun searchPokemon(name: String) {
        viewModel.userIntent.offer(HomeIntent.GetPokemonByName(name))
    }

    override fun onPause() {
        super.onPause()
        floatingToast?.dismiss()
    }

}
