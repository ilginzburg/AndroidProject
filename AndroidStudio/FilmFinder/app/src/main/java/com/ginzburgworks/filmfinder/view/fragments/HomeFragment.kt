package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.Film
import com.ginzburgworks.filmfinder.data.PageManager
import com.ginzburgworks.filmfinder.data.PreferenceProvider
import com.ginzburgworks.filmfinder.data.SearchController
import com.ginzburgworks.filmfinder.databinding.FragmentHomeBinding
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

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    @Inject
    lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var pageManager: PageManager
    private val searchController by lazy { initSearchView() }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[HomeFragmentViewModel::class.java]
    }

    @Singleton
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initAnimation()
        initRecycler()
        initSearchView()
        initPullToRefresh()
        initRefreshOnChange()
        subscribeToProgressBarChanges()
        subscribeToFilmsListChanges()
    }


    private fun subscribeToFilmsListChanges() {
        viewModel.filmsListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewModel.requestNextPageFromNetwork()
            }
            if (it.isNotEmpty()) {
                filmsAdapter.addItems(it)
            }
        }

    }

    private fun subscribeToProgressBarChanges() {
        fragmentHomeBinding.progressBar.bringToFront()
        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            fragmentHomeBinding.progressBar.isVisible = it
        }

    }

    private fun initSearchView(): SearchController {
        return SearchController(filmsAdapter, viewModel).apply {
            fragmentHomeBinding.searchView.setOnClickListener {
                filmsAdapter.saveItemsForSearch(viewModel)
                fragmentHomeBinding.searchView.isIconified = false
            }
            fragmentHomeBinding.searchView.setOnCloseListener {
                if (viewModel.itemsForSearch.size > 0) {
                    filmsAdapter.clearItems()
                    filmsAdapter.addItems(viewModel.itemsForSearch)
                    viewModel.itemsForSearch.clear()
                }
                true
            }
            fragmentHomeBinding.searchView.setOnQueryTextListener(this)
        }
    }

    private fun initRecycler() {
        fragmentHomeBinding.mainRecycler.apply {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
            addItemDecoration(decorator)
            pageManager = PageManager(viewModel, layoutManager as LinearLayoutManager)
            addOnScrollListener(pageManager)
        }
        filmsAdapter.setListener(object : FilmListRecyclerAdapter.OnItemClickListener {
            override fun onClick(film: Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
    }

    private fun initPullToRefresh() {
        swipeRefreshLayout =
            activity?.findViewById(R.id.pull_to_refresh) ?: fragmentHomeBinding.pullToRefresh
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
        viewModel.onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == PreferenceProvider.KEY_FILMS_CATEGORY)
                    updateAdapterBuffer()
            }
        viewModel.interactor.preferenceProvider.registerListener(viewModel.onSharedPreferenceChangeListener)
    }

    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            fragmentHomeBinding.homeFragmentRoot,
            requireActivity(),
            ANIM_POSITION
        )
    }
}



