package co.tiagoaguiar.netflixremake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(),CategoryTask.Callback {
    private lateinit var progress: ProgressBar
    private val categories = mutableListOf<Category>()
    private lateinit var adapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress = findViewById(R.id.progress_circular_main)



        adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        CategoryTask(this).execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=cd7d9240-1ecd-4269-84c5-46b3634cafc2")
    }

    override fun onPreExecute() {
        progress.visibility = View.VISIBLE

    }
    override fun onResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()
        progress.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
    }


}