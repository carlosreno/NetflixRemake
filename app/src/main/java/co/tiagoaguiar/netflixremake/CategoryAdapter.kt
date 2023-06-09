package co.tiagoaguiar.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category

open class CategoryAdapter
    (
    private val categories: List<Category>,
    private val onItemClickListener: (Int) -> Unit
    ) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            val txt_title: TextView = itemView.findViewById(R.id.text_categoria)
            txt_title.text = category.name
            val rvCategory: RecyclerView = itemView.findViewById(R.id.rv_category)
            rvCategory.layoutManager = LinearLayoutManager(itemView.context,RecyclerView.HORIZONTAL,false)
            rvCategory.adapter = MovieAdapter(R.layout.movie_item,category.movies, onItemClickListener)
        }

    }

}