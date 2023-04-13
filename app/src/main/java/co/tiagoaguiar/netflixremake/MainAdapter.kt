package co.tiagoaguiar.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie
import com.squareup.picasso.Picasso

open class MovieAdapter(@LayoutRes private val layoutId: Int,
                        private val moveis: List<Movie>,
                        private val onItemClickListener: ((Int) -> Unit)? = null)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moveis[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return moveis.size
    }
    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val movieUrl: ImageView = itemView.findViewById(R.id.movie_item_id)
            movieUrl.setOnClickListener {
                onItemClickListener?.invoke(movie.id)
            }
            Picasso.get().load(movie.coverUrl).into(movieUrl)
        }

    }

}