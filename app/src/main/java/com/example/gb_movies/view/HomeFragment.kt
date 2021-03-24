package com.example.gb_movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.R
import com.example.gb_movies.databinding.FragmentHomeBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.viewmodel.AppState
import com.example.gb_movies.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    companion object {
        val TAG: String = HomeFragment::class.java.name
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                binding.loadingLayout.visibility = View.GONE
                setData(movieData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getMovieFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(movieData: Movie) {
        binding.textView1.text = movieData.name
        binding.textView2.text = movieData.description
    }
}