package com.example.gb_movies.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.R
import com.example.gb_movies.databinding.FragmentDetailsBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.utils.showSnackBar
import com.example.gb_movies.viewmodel.AppState
import com.example.gb_movies.viewmodel.DetailsViewModel
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var movie: Movie
    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
            this.movie = movie
        }
        viewModel.liveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromRemoteSource(movie.id)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                binding.detailsView.visibility = View.VISIBLE
                binding.detailsLoadingLayout.visibility = View.GONE
                movie = appState.data as Movie
                displayMovie(movie)
            }
            is AppState.Loading -> {
                binding.detailsView.visibility = View.GONE
                binding.detailsLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.detailsView.visibility = View.VISIBLE
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) {viewModel.getMovieFromRemoteSource(movie.id) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayMovie(movie: Movie) {
        with(binding) {
            tileTextView.text = movie.title
            genresTextView.text = movie.genresToString()
            runtimeTextView.text = String.format("%d мин", movie.runTime)
            voteTextView.text = String.format("%.1f (%d)", movie.vote, movie.voteCount)
            budgetTextView.text = String.format("Бюджет: %d $", movie.budget)
            revenueTextView.text = String.format("Доход: %d $", movie.revenue)
            releaseTextView.text = movie.releaseDate
            overviewTextView.text = movie.overview

            movie.poster?.let{
                Picasso
                    .get()
                    .load("https://image.tmdb.org/t/p/w500$it")
                    .into(posterImageView)
            }

        }
    }

    companion object {
        const val BUNDLE_EXTRA = "movie"
        val TAG: String = DetailsFragment::class.java.name

        fun newInstance(movie: Movie) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_EXTRA, movie)
                }
            }

    }
}