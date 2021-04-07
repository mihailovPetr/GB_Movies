package com.example.gb_movies.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gb_movies.R
import com.example.gb_movies.model.Movie


class MoviesAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private var moviesData: List<Movie> = listOf()

    fun setMoviesData(data: List<Movie>) {
        moviesData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movies_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(moviesData[position])
    }

    override fun getItemCount(): Int {
        return moviesData.size
    }

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var movie: Movie
        private val textView = itemView.findViewById<TextView>(R.id.moviesRecyclerItemTextView)

        init {
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(movie)
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            textView.text = movie.title
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }
}
