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
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.ITEMS_AFTER_START
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.ITEMS_BEFORE_END
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.NEXT_PAGE
import com.ginzburgworks.filmfinder.domain.PagesController.Companion.PAGE_SIZE
import com.ginzburgworks.filmfinder.utils.AnimationHelper
import com.ginzburgworks.filmfinder.utils.TopSpacingItemDecoration
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.filmfinder.viewmodels.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

private const val ANIM_POSITION = 1
private const val DECORATOR_PADDING = 8
private const val ERROR_MSG = "ERROR: Data source not responding"
private const val SEARCH_ERROR_MSG = "Что-то пошло не так"
private const val SEARCH_DEBOUNCE_TIMEOUT = 300L

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel by activityViewModels<HomeFragmentViewModel>()
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.disposables.forEach { it?.addTo(autoDisposable) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.let {
            it.homeFragment = this
            it.viewModel = viewModel
            it.list = viewModel.itemsSavedBeforeSearch
        }
        initComponents()
    }

    private fun initComponents() {
        initAnimation()
        initRecycler()
        initSearchView()
        initProgressBar()
    }

    override fun onStart() {
        super.onStart()
        initDataTransaction()
    }

    private fun initDataTransaction() {
        subscribeToNetworkErrorMessages()
        subscribeOnDataChanges()
    }

    private fun subscribeOnDataChanges() = with(viewModel) {
        requestNextPage().observeOn(AndroidSchedulers.mainThread()).filter { it.isNotEmpty() }
            .switchIfEmpty(requestNextPage()).subscribe({
                filmsAdapter.addItems(it)
            }, {
                errorEvent.value = ERROR_MSG
                it.printStackTrace()
            }).addTo(autoDisposable)
    }

    private fun initProgressBar() {
        binding.progressBar.bringToFront()
        subscribeOnProgressBar()
    }

    private fun subscribeOnProgressBar() = viewModel.showProgressBar.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe({
            binding.progressBar.isVisible = it
        }, {
            it.printStackTrace()
        }).addTo(autoDisposable)


    private fun subscribeToNetworkErrorMessages() =
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }


    private fun initSearchView() = Observable.create(ObservableOnSubscribe<String> { subscriber ->
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { subscriber.onNext(it) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) viewModel.reloadOnSearch(viewModel.itemsSavedBeforeSearch)
                else subscriber.onNext(newText)
                return false
            }
        })
    }).subscribeOn(Schedulers.io()).map {
        it.lowercase(Locale.getDefault()).trim()
    }.debounce(SEARCH_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS).filter {
        it.isNotBlank()
    }.flatMap {
        viewModel.requestSearchResults(it)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(onError = {
        Toast.makeText(requireContext(), SEARCH_ERROR_MSG, Toast.LENGTH_SHORT).show()
    }, onNext = { viewModel.reloadOnSearch(it) }).addTo(autoDisposable)


    fun onSearchStart() {
        saveItems()
        binding.searchView.isIconified = false
    }

    private fun saveItems() = viewModel.filmsAdapter.saveItemsForSearch(viewModel)

    private fun initRecycler() = binding.mainRecycler.apply {
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

    private fun addDecoration(recycler: RecyclerView) {
        val decorator = TopSpacingItemDecoration(DECORATOR_PADDING)
        recycler.addItemDecoration(decorator)
    }

    private fun launchDetailsFragment(film: Film) =
        (requireActivity() as MainActivity).launchDetailsFragment(film)

    private fun bottomItemPosition() = linearLayoutManager.findLastCompletelyVisibleItemPosition()

    private fun startPagePosition() = linearLayoutManager.itemCount - PAGE_SIZE + ITEMS_AFTER_START

    private fun endPagePosition() = linearLayoutManager.itemCount - ITEMS_BEFORE_END

    private fun isPageOnStart() = bottomItemPosition() == startPagePosition()

    private fun isPageOnEnd() = bottomItemPosition() == endPagePosition()

    private fun isNeedToRequestNextPage(verticalDisplacement: Int): Boolean {
        if (isPageOnStart()) updateStateOnStart()
        return isPageOnEnd() && !viewModel.isPageRequested && isScrollingDown(verticalDisplacement) && isNotLastPage()
    }

    private fun updateStateOnStart() {
        incrementNextPageIfNeed()
        false.also { viewModel.isPageRequested = it }
    }

    private fun isNotLastPage() = NEXT_PAGE < viewModel.getTotalNumberOfPages()

    private fun isScrollingDown(verticalDisplacement: Int) = verticalDisplacement > 0

    private fun incrementNextPageIfNeed() {
        if (viewModel.isPageRequested) ++NEXT_PAGE
    }

    private fun initAnimation() = AnimationHelper.performFragmentCircularRevealAnimation(
        binding.homeFragmentRoot, requireActivity(), ANIM_POSITION
    )

}



