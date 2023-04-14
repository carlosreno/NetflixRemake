package co.tiagoaguiar.netflixremake

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import co.tiagoaguiar.netflixremake.util.DowloadimageTask
import co.tiagoaguiar.netflixremake.util.MovieTask
import com.squareup.picasso.Picasso
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MovieActivity : AppCompatActivity(), MovieTask.Callback {
    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtCast: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MovieAdapter
    private lateinit var moves: MutableList<Movie>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        txtTitle = findViewById(R.id.movie_title)
        txtDesc = findViewById(R.id.movie_desc)
        txtCast = findViewById(R.id.movie_cast)
        val rv: RecyclerView = findViewById(R.id.rv_movie)

        val id = intent?.getIntExtra("id", 0) ?: throw IllegalStateException("not found id")
        val url =
            "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=cd7d9240-1ecd-4269-84c5-46b3634cafc2"
        progressBar = findViewById(R.id.movie_progress)
        MovieTask(this).execute(url)

        moves = mutableListOf<Movie>()

        adapter = MovieAdapter(R.layout.movie_item_similar, moves)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = adapter
        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onResult(movieDetail: MovieDetail) {
        progressBar.visibility = View.GONE
        txtTitle.text = movieDetail.movie.title
        txtDesc.text = movieDetail.movie.desc
        txtCast.text = movieDetail.movie.cast

        moves.clear()
        moves.addAll(movieDetail.similar)
        adapter.notifyDataSetChanged()
        DowloadimageTask(object : DowloadimageTask.Callback{
            override fun onResult(bitmap: Bitmap) {
                val layerDrawable: LayerDrawable =
                    ContextCompat.getDrawable(this@MovieActivity, R.drawable.shadows) as LayerDrawable
                val movieCover = BitmapDrawable(resources, bitmap)
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

                val movieImg: ImageView = findViewById(R.id.movie_img)
                movieImg.setImageDrawable(layerDrawable)
            }

        }).execute(movieDetail.movie.coverUrl)
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}