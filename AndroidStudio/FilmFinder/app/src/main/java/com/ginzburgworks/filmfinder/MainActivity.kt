package com.ginzburgworks.filmfinder

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil


class MainActivity : AppCompatActivity() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filmsDataBase = mutableListOf<Film>(
            Film(
                1,
                "American Gangster",
                R.drawable.american_gangster,
                "An outcast New York City cop is charged with bringing down Harlem drug lord Frank Lucas, whose real life inspired this partly biographical film."
            ),
            Film(
                2,
                "Casino Royale",
                R.drawable.casino_royale,
                "Short behind the scenes making-of documentary about the filming of the sinking house palazzo and the Venetian piazza chase from the James Bond movie Casino Royale."
            ),
            Film(
                3,
                "Catch Me If You Can",
                R.drawable.catch_me_if_you_can,
                "Barely 21 yet, Frank is a skilled forger who has passed as a doctor, lawyer and pilot. FBI agent Carl becomes obsessed with tracking down the con man, who only revels in the pursuit."
            ),
            Film(
                4,
                "Clockwork Orange",
                R.drawable.clockwork_orange,
                "In the future, a sadistic gang leader is imprisoned and volunteers for a conduct-aversion experiment, but it doesn't go as planned."
            ),
            Film(
                5,
                "Desperado",
                R.drawable.desperado,
                "Former musician and gunslinger El Mariachi arrives at a small Mexican border town after being away for a long time. His past quickly catches up with him and he soon gets entangled with the local drug kingpin Bucho and his gang."
            ),
            Film(
                6,
                "Die Hard 2",
                R.drawable.die_hard_2,
                "John McClane just wanted to fetch his wife Holly at the airport but when he arrives there the whole place is overrun by terrorists. Now McClane has to battle hundreds of them in order to prevent their evil plans."
            ),
            Film(
                7,
                "Jurassic Park",
                R.drawable.jurassic_park,
                "A pragmatic paleontologist visiting an almost complete theme park is tasked with protecting a couple of kids after a power failure causes the park's cloned dinosaurs to run loose."
            )
        )

//находим наш RV
        main_recycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        //Создаем бандл и кладем туда объект с данными фильма
                        val bundle = Bundle()
                        //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                        //передаваемый объект
                        bundle.putParcelable("film", film)
                        //Запускаем наше активити
                        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                        //Прикрепляем бандл к интенту
                        intent.putExtras(bundle)
                        //Запускаем активити через интент
                        startActivity(intent)
                    }
                })
            adapter = filmsAdapter



            layoutManager = LinearLayoutManager(this@MainActivity)
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        filmsAdapter.addItems(filmsDataBase)

        initNavigation()
    }


    private fun initNavigation() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    showSnackbarDefault("Избранное")
                    true
                }
                R.id.watch_later -> {
                    showSnackbarDefault("Посмотреть похже")
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

    private fun showSnackbarDefault(msg: String) {
        val snackbar = Snackbar.make(root_container, msg, Snackbar.LENGTH_SHORT);
        val view = snackbar.view
        val txtv = view.findViewById<View>(R.id.snackbar_text) as TextView
        txtv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        txtv.setTextColor(ContextCompat.getColor(this, R.color.black))
        snackbar.show();
    }


}