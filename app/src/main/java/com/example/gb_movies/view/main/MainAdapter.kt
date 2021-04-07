package com.example.gb_movies.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gb_movies.R
import com.example.gb_movies.model.Movie
import com.example.gb_movies.model.MoviesGroup

class MainAdapter(private var onItemViewClickListener: OnMovieItemClickListener?) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var movieGroupsData: List<MoviesGroup> = listOf()

    fun setData(data: List<MoviesGroup>) {
        movieGroupsData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movieGroupsData[position])
    }

    override fun getItemCount(): Int {
        return movieGroupsData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title = itemView.findViewById<TextView>(R.id.mainRecyclerTitleTextView)
        private val description =
            itemView.findViewById<TextView>(R.id.mainRecyclerDescriptionTextView)

        private val adapter = MoviesAdapter(object : MoviesAdapter.OnItemViewClickListener {
            override fun onItemViewClick(movie: Movie) {
                onItemViewClickListener?.onMovieClick(movie)
            }
        })
        private val recyclerView =
            itemView.findViewById<RecyclerView>(R.id.mainRecyclerItemsContainer).also {
                it.setHasFixedSize(true)
                it.adapter = adapter
            }


        fun bind(group: MoviesGroup) {
            group.movies?.let { adapter.setMoviesData(it) }
            title.text = group.title
            description.text = group.description
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    interface OnMovieItemClickListener { //TODO: исправить
        fun onMovieClick(movie: Movie)
    }
}