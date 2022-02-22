package com.ginzburgworks.filmfinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ginzburgworks.filmfinder.AutoDisposable
import com.ginzburgworks.filmfinder.addTo
import com.ginzburgworks.filmfinder.data.local.Film
import com.ginzburgworks.filmfinder.databinding.FragmentHomeBinding
import com.ginzburgworks.filmfinder.domain.PagesController
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.ITEMS_AFTER_START
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.ITEMS_BEFORE_END
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.PAGE_SIZE
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import com.ginzburgworks.filmfinder.utils.TopSpacingItemDecoration
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

private const val ANIM_POSITION = 1
private const val DECORATOR_PADDING = 8
private const val ERROR_MSG = "ERROR: Data source not responding"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val viewModel by activityViewModels<HomeFragmentViewModel>()

    private val autoDisposable = AutoDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.homeFragment = this
        _binding?.viewModel = viewModel
        initComponents()
    }

    private fun initComponents() {
        initAnimation()
        initRecycler()
        initSearchView()
        binding.progressBar.bringToFront()
        subscribeToNetworkErrorMessages()
        subscribeOnDataChanges()
        subscribeOnProgressBar()
        viewModel.requestNextPage()
    }

    private fun subscribeOnDataChanges() {
        viewModel.filmsListData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { viewModel.errorEvent.value = ERROR_MSG }
            .subscribe { list ->
                viewModel.filmsAdapter.addItems(list)
            }.addTo(autoDisposable)
    }

    private fun subscribeOnProgressBar() {
        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }.addTo(autoDisposable)
    }

    private fun subscribeToNetworkErrorMessages() {
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    viewModel.reloadAfterSearch()
                    return true
                }
                val result = viewModel.itemsForSearch.filter {
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                }
                viewModel.reloadOnTextChange(result)
                return true
            }
        })
    }

    private fun initRecycler() {
        binding.mainRecycler.apply {
            adapter = viewModel.filmsAdapter
            layoutManager = LinearLayoutManager(requireActivity() as MainActivity)
            addDecoration(this)
            linearLayoutManager = layoutManager as LinearLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (isNeedToRequestNextPage(dy)) viewModel.requestNextPage()
                }
            })
        }.also { viewModel.filmsAdapter.onItemClick = { launchDetailsFragment(it) } }
    }

    private fun addDecoration(recycler: RecyclerView) {
        val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
        recycler.addItemDecoration(decorator)
    }

    private fun launchDetailsFragment(film: Film) {
        (requireActivity() as MainActivity).launchDetailsFragment(film)
    }

    fun saveItems() {
        viewModel.filmsAdapter.saveItemsForSearch(viewModel)
        binding.searchView.isIconified = false
    }

    private fun bottomItemPosition() = linearLayoutManager.findLastCompletelyVisibleItemPosition()

    private fun startPagePosition() = linearLayoutManager.itemCount - PAGE_SIZE + ITEMS_AFTER_START

    private fun endPagePosition() = linearLayoutManager.itemCount - ITEMS_BEFORE_END

    private fun isPageOnStart() = bottomItemPosition() == startPagePosition()

    private fun isPageOnEnd() = bottomItemPosition() == endPagePosition()

    private fun isNeedToRequestNextPage(verticalDisplacement: Int): Boolean {
        if (isPageOnStart())
            updateStateOnStart()
        return isPageOnEnd() &&
                !viewModel.isPageRequested &&
                isScrollingDown(verticalDisplacement) &&
                isNotLastPage()
    }

    private fun updateStateOnStart() {
        incrementNextPageIfNeed()
        false.also { viewModel.isPageRequested = it }
    }

    private fun isNotLastPage() = PagesController.NEXT_PAGE < viewModel.getTotalNumberOfPages()

    private fun isScrollingDown(verticalDisplacement: Int) = verticalDisplacement > 0

    private fun incrementNextPageIfNeed() {
        if (viewModel.isPageRequested) ++PagesController.NEXT_PAGE
    }

    private fun initAnimation() {
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.homeFragmentRoot, requireActivity(), ANIM_POSITION
        )
    }
}



