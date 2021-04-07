package com.example.gb_movies.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.R
import com.example.gb_movies.databinding.FragmentHomeBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.view.details.DetailsFragment
import com.example.gb_movies.viewmodel.AppState
import com.example.gb_movies.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val adapter = MainAdapter(object : MainAdapter.OnMovieItemClickListener {
        override fun onMovieClick(movie: Movie) {
            onMovieItemClick(movie)
        }
    })


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getMovieGroupsFromLocalSource()
    }

    private fun initRecyclerViewOld() {
//        binding.moviesRecyclerView.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.moviesRecyclerView.layoutManager = layoutManager
//
//        binding.moviesRecyclerView.adapter = adapter
//
//        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
//        itemDecoration.setDrawable(resources.getDrawable(R.drawable.separator, null))
//        binding.moviesRecyclerView.addItemDecoration(itemDecoration)
    }

    private fun initRecyclerView() {
        with(binding) {
            mainRecyclerView.setHasFixedSize(true)
            mainRecyclerView.adapter = adapter
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(appState.moviesData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.homeFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) { viewModel.getMovieGroupsFromLocalSource() }
            }
        }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        length: Int = Snackbar.LENGTH_INDEFINITE,
        action: ((View) -> Unit)? = null
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
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
