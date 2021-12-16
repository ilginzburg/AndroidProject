package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.data.PreferenceProvider
import com.ginzburgworks.filmfinder.data.SearchController
import com.ginzburgworks.filmfinder.databinding.FragmentHomeBinding
import com.ginzburgworks.filmfinder.domain.Film
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import com.ginzburgworks.filmfinder.utils.TopSpacingItemDecoration
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import javax.inject.Inject
import javax.inject.Singleton


private const val ANIM_POSITION = 1
private const val DECORATOR_PADDING = 8

class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding
    private val searchController by lazy { initSearchView() }

    @Singleton
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[HomeFragmentViewModel::class.java]
    }

    @Inject
    lateinit var preferenceProvider: PreferenceProvider

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var pageManager: PageManager
    private lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.homeFragmentRoot,
            requireActivity(),
            ANIM_POSITION
        )
        initRecycler()
        initSearchView()
        swipeRefreshLayout = activity?.findViewById(R.id.pull_to_refresh) ?: binding.pullToRefresh
        initPullToRefresh()
        initRefreshOnChange()
        viewModel.filmsListLiveData.observe(viewLifecycleOwner, {
            filmsAdapter.addItems(it)
        })
    }


    private fun initSearchView(): SearchController {
        return SearchController(filmsAdapter, viewModel).apply {
            binding.searchView.setOnClickListener {
                saveItemsForSearch()
                binding.searchView.isIconified = false;
            }
            binding.searchView.setOnCloseListener {
                if (viewModel.itemsForSearch.size > 0) {
                    filmsAdapter.clearItems()
                    filmsAdapter.addItems(viewModel.itemsForSearch)
                    viewModel.itemsForSearch.clear()
                }
                true
            }
            binding.searchView.setOnQueryTextListener(this)
        }
    }

    private fun initRecycler() {
        binding.mainRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
            addItemDecoration(decorator)
            pageManager = PageManager(viewModel, layoutManager as LinearLayoutManager)
            addOnScrollListener(pageManager)
        }
    }

    private fun initPullToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            updateAdapterBuffer()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun updateAdapterBuffer() {
        pageManager.restartPages()
        filmsAdapter.clearItems()
    }


    private fun initRefreshOnChange() {
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                updateAdapterBuffer()
            }
        preferenceProvider.registerListener(onSharedPreferenceChangeListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this::onSharedPreferenceChangeListener.isInitialized)
            preferenceProvider.unRegisterListener(onSharedPreferenceChangeListener)
    }
}



