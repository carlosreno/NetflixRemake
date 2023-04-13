package co.tiagoaguiar.netflixremake

import android.content.Context
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import co.tiagoaguiar.netflixremake.util.MovieTask

class MovieActivity : AppCompatActivity(), MovieTask.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val txtTitle: TextView = findViewById(R.id.movie_title)
        val txtDesc: TextView = findViewById(R.id.movie_desc)
        val txtCast: TextView = findViewById(R.id.movie_cast)
        val rv: RecyclerView = findViewById(R.id.rv_movie)

        val id = intent?.getIntExtra("id",0) ?: throw IllegalStateException("not found id")
        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=cd7d9240-1ecd-4269-84c5-46b3634cafc2"
        MovieTask(this).execute(url)
        txtTitle.text="Maria Broz"
        txtDesc.text = "Adaptação do jogo homônimo. Acompanhe" +
                " o encanador Mario e seu irmão Luigi tentando salvar uma" +
                " princesa capturada. Para alcançar o objetivo e libertá-la," +
                " os dois precisarão embarcar em uma viagem por um labirinto subterrâneo"
        txtCast.text= getString(R.string.cast,"Mario, Luigi, Princesa Peach, Bowser, Toad, Donkey Kong e Yoshi")
        val moveis = mutableListOf<Movie>()

        rv.layoutManager = GridLayoutManager(this,3)
        rv.adapter = MovieAdapter(R.layout.movie_item_similar,moveis)
        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=null
        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this,R.drawable.shadows) as LayerDrawable

        val movieCover = ContextCompat.getDrawable(this,R.drawable.mario_19_9)

        layerDrawable.setDrawableByLayerId(R.id.cover_drawable,movieCover)
        val movieImg: ImageView = findViewById(R.id.movie_img)
        movieImg.setImageDrawable(layerDrawable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {

    }

    override fun onResult(movieDetail: MovieDetail) {
    Log.i("",movieDetail.toString())
    }

    override fun onFailure(message: String) {

    }
}