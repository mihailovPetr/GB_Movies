package com.example.gb_movies.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.databinding.FragmentDetailsBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.viewmodel.AppState
import com.example.gb_movies.viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
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
            binding.textView.text = movie.name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getMoviesFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                //binding.loadingLayout.visibility = View.GONE
                //setData(movieData)
            }
            is AppState.Loading -> {
                //binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                // binding.loadingLayout.visibility = View.GONE
                //Snackbar
                //.make(binding.mainView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                //.setAction(getString(R.string.reload)) { viewModel.getMovieFromLocalSource() }
                //.show()
            }
        }
    }

    private fun setData(movieData: Movie) {
        //binding.textView1.text = movieData.name
        //binding.textView2.text = movieData.description
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