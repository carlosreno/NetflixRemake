package co.tiagoaguiar.netflixremake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val  moveis = mutableListOf<Movie>()
        val categories = mutableListOf<Category>()
        for (j in 0 until 10){
            for (i in 0 until 5){
                val movie = Movie(coverUrl = R.drawable.placeholder_bg)
                moveis.add(movie)
            }
            val category = Category("categoria $j",moveis)
            categories.add(category)
        }

        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }




}