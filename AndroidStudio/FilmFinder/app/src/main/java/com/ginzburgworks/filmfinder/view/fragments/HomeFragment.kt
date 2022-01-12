package com.ginzburgworks.filmfinder.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ginzburgworks.filmfinder.App
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.data.local.shared.KEY_FILMS_CATEGORY
import com.ginzburgworks.filmfinder.databinding.FragmentHomeBinding
import com.ginzburgworks.filmfinder.domain.PagesController
import com.ginzburgworks.filmfinder.domain.SearchController
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import com.ginzburgworks.filmfinder.utils.TopSpacingItemDecoration
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.view.rv_adapters.FilmListRecyclerAdapter
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val ANIM_POSITION = 1
private const val DECORATOR_PADDING = 8
private const val FIRST_INDEX_IN_LIST = 0

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    @Inject
    lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var pagesController: PagesController
    private val searchController by lazy { initSearchView() }

    private val homeFragmentViewModel by lazy {
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
        fragmentHomeBinding.progressBar.bringToFront()
        subscribeToNetworkErrorMessages()
        getPage()
    }

    private fun getPage() {
        pagesController.isPageRequested = true
        exposeProgressBar()
        if (homeFragmentViewModel.isLocalDataSourceNeedToUpdate())
            getPageFromRemote()
        getPageFromLocal()
    }

    private fun getPageFromRemote() = viewLifecycleOwner.lifecycleScope.launch {
        homeFragmentViewModel.requestNextPageFromRemote()
        for (element in homeFragmentViewModel.pageOfFilmsFromRemoteDataSourceToUI) {
            filmsAdapter.addItems(element)
            break;
        }
    }

    private fun getPageFromLocal() = viewLifecycleOwner.lifecycleScope.launch {
        homeFragmentViewModel.requestNextPageFromLocal()
        for (element in homeFragmentViewModel.pageOfFilmsFromLocalDataSourceToUI) {
            if (element.isEmpty())
                getPageFromRemote()
            else
                filmsAdapter.addItems(element)
            break;
        }
    }

    private fun exposeProgressBar() = viewLifecycleOwner.lifecycleScope.launch {
        for (element in homeFragmentViewModel.showProgressBar) {
            fragmentHomeBinding.progressBar.isVisible = element
            if (!element)
                break
        }
    }

    private fun subscribeToNetworkErrorMessages() {
        homeFragmentViewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun initSearchView(): SearchController {
        return SearchController(filmsAdapter, homeFragmentViewModel).apply {
            fragmentHomeBinding.searchView.setOnClickListener {
                filmsAdapter.saveItemsForSearch(homeFragmentViewModel)
                fragmentHomeBinding.searchView.isIconified = false
            }
            fragmentHomeBinding.searchView.setOnCloseListener {
                if (homeFragmentViewModel.itemsForSearch.size > 0) {
                    filmsAdapter.clearItems()
                    filmsAdapter.addItems(homeFragmentViewModel.itemsForSearch)
                    homeFragmentViewModel.itemsForSearch.clear()
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
            pagesController =
                PagesController(homeFragmentViewModel, layoutManager as LinearLayoutManager)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (pagesController.isNeedToRequestNextPage(dy)) {
                        getPage()
                        ++PagesController.NEXT_PAGE
                    }
                    if (pagesController.isPageOnStart())
                        pagesController.isPageRequested = false
                }
            })
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
            restartPages()
            getPage()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun restartPages() {
        filmsAdapter.clearItems()
        PagesController.NEXT_PAGE = PagesController.FIRST_PAGE
    }

    private fun initRefreshOnChange() {
        homeFragmentViewModel.onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == KEY_FILMS_CATEGORY)
                    restartPages()
            }
        homeFragmentViewModel.registerOnChangeListener()
    }


    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            fragmentHomeBinding.homeFragmentRoot,
            requireActivity(),
            ANIM_POSITION
        )
    }
}



