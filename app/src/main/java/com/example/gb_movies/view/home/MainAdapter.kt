package com.example.gb_movies.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gb_movies.R
import com.example.gb_movies.model.Movie
import com.example.gb_movies.model.testGetMovies

class MainAdapter() : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        //TODO:
        return 10
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title = itemView.findViewById<TextView>(R.id.mainRecyclerTitleTextView)
        private val description =
            itemView.findViewById<TextView>(R.id.mainRecyclerDescriptionTextView)
        private val recyclerView =
            itemView.findViewById<RecyclerView>(R.id.mainRecyclerItemsContainer)

        fun bind() {
            title.text = "Загаловок"
            description.text = "Описание"

            //TODO:
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = MoviesAdapter(object : MoviesAdapter.OnItemViewClickListener {
                    override fun onItemViewClick(movie: Movie) {}
                }).apply {
                    setMoviesData(testGetMovies())
                }
            }
        }


    }
}