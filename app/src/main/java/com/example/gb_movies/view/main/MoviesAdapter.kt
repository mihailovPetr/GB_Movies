package com.example.gb_movies.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.gb_movies.R
import com.example.gb_movies.model.Movie
import com.squareup.picasso.Picasso


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
        private val imageView = itemView.findViewById<AppCompatImageView>(R.id.moviesRecyclerItemImageView)

        init {
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(movie)
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            textView.text = movie.title
            movie.poster?.let{
                Picasso
                    .get()
                    .load("https://image.tmdb.org/t/p/w342$it")
                    .into(imageView)
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }
}
