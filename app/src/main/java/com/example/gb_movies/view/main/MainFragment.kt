package com.example.gb_movies.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.R
import com.example.gb_movies.databinding.FragmentMainBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.model.MoviesGroup
import com.example.gb_movies.utils.showSnackBar
import com.example.gb_movies.view.details.DetailsFragment
import com.example.gb_movies.viewmodel.AppState
import com.example.gb_movies.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val adapter = MainAdapter(object : MainAdapter.OnMovieItemClickListener {
        override fun onMovieClick(movie: Movie) {
            onMovieItemClick(movie)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getMovieGroupsFromLocalSource()
    }

    private fun initRecyclerView() {
        with(binding) {
            mainRecyclerView.setHasFixedSize(true)
            mainRecyclerView.adapter = adapter
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(appState.data as List<MoviesGroup>)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) { viewModel.getMovieGroupsFromLocalSource() }
            }
        }
    }

    private fun onMovieItemClick(movie: Movie) {
        val manager = activity?.supportFragmentManager
        manager?.beginTransaction()
            ?.replace(R.id.fragment_container, DetailsFragment.newInstance(movie))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}
