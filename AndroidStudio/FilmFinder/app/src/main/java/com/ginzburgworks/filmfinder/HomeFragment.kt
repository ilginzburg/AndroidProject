package com.ginzburgworks.filmfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

private const val ANIM_POSITION = 1
private const val DECORATOR_PADDING = 8

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val filmsDataBase = mutableListOf(
        Film(
            1,
            "American Gangster",
            R.drawable.american_gangster,
            "An outcast New York City cop is charged with bringing down Harlem drug lord Frank Lucas, whose real life inspired this partly biographical film.",
            6.2f
        ),
        Film(
            2,
            "Casino Royale",
            R.drawable.casino_royale,
            "Short behind the scenes making-of documentary about the filming of the sinking house palazzo and the Venetian piazza chase from the James Bond movie Casino Royale.",
            7.1f
        ),
        Film(
            3,
            "Catch Me If You Can",
            R.drawable.catch_me_if_you_can,
            "Barely 21 yet, Frank is a skilled forger who has passed as a doctor, lawyer and pilot. FBI agent Carl becomes obsessed with tracking down the con man, who only revels in the pursuit.",
            8.3f
        ),
        Film(
            4,
            "Clockwork Orange",
            R.drawable.clockwork_orange,
            "In the future, a sadistic gang leader is imprisoned and volunteers for a conduct-aversion experiment, but it doesn't go as planned.",
            9.1f
        ),
        Film(
            5,
            "Desperado",
            R.drawable.desperado,
            "Former musician and gunslinger El Mariachi arrives at a small Mexican border town after being away for a long time. His past quickly catches up with him and he soon gets entangled with the local drug kingpin Bucho and his gang.",
            8.6f
        ),
        Film(
            6,
            "Die Hard 2",
            R.drawable.die_hard_2,
            "John McClane just wanted to fetch his wife Holly at the airport but when he arrives there the whole place is overrun by terrorists. Now McClane has to battle hundreds of them in order to prevent their evil plans.",
            3.1f
        ),
        Film(
            7,
            "Jurassic Park",
            R.drawable.jurassic_park,
            "A pragmatic paleontologist visiting an almost complete theme park is tasked with protecting a couple of kids after a power failure causes the park's cloned dinosaurs to run loose.",
            1.2f
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(
            home_fragment_root,
            requireActivity(),
            ANIM_POSITION
        )
        initSearchView()
        initRecycler()
        filmsAdapter.addItems(filmsDataBase)
    }

    private fun initSearchView() {
        search_view.setOnClickListener {
            search_view.isIconified = false
        }

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    filmsAdapter.addItems(filmsDataBase)
                    return true
                }

                val result = filmsDataBase.filter {
                    it.title.toLowerCase(Locale.getDefault())
                        .contains(newText.toLowerCase(Locale.getDefault()))
                }
                filmsAdapter.addItems(result)
                return true
            }
        })
    }

    private fun initRecycler() {
        main_recycler.apply {
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
        }
    }
}