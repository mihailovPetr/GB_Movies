package com.example.gb_movies.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.gb_movies.R
import com.example.gb_movies.databinding.FragmentDetailsBinding
import com.example.gb_movies.model.Movie
import com.example.gb_movies.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_MOVIE_EXTRA = "MOVIE"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var movie: Movie
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
//    private val onLoadListener: MovieLoader.MovieLoaderListener =
//        object : MovieLoader.MovieLoaderListener {
//
//            override fun onLoaded(movieDTO: MovieDTO) {
//                displayMovie(movieDTO)
//            }
//
//            override fun onFailed(throwable: Throwable) {
//            }
//        }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayMovie(
                    intent.getParcelableExtra(
                        DETAILS_MOVIE_EXTRA
                    )
                )
                else -> onLoadFailed(intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
            this.movie = movie
        }

        binding.detailsView.visibility = View.GONE
        binding.detailsLoadingLayout.visibility = View.VISIBLE
//        val loader = MovieLoader(onLoadListener, movie.id)
//        loader.loadMovie()

        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }

        getMovie()
    }

    private fun getMovie() {
        binding.detailsView.visibility = View.GONE
        binding.detailsLoadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(
                    MOVIE_ID_EXTRA,
                    movie.id
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

//    private fun displayMovie(movieDTO: MovieDTO) {
//        with(binding) {
//            detailsView.visibility = View.VISIBLE
//            detailsLoadingLayout.visibility = View.GONE
//
//            tileTextView.text = movieDTO.title
//            genresTextView.text = movieDTO.getGenres()
//            runtimeTextView.text = String.format("%d мин", movieDTO.runtime)
//            voteTextView.text = String.format("%.1f (%d)", movieDTO.vote_average, movieDTO.vote_count)
//            budgetTextView.text = String.format("Бюджет: %d $", movieDTO.budget)
//            revenueTextView.text = String.format("Доход: %d $", movieDTO.revenue)
//            releaseTextView.text = movieDTO.release_date
//            overviewTextView.text = movieDTO.overview
//        }
//    }

    private fun displayMovie(movie: Movie?) {
        with(binding) {
            detailsView.visibility = View.VISIBLE
            detailsLoadingLayout.visibility = View.GONE

            movie?.let {
                tileTextView.text = movie.title
                genresTextView.text = movie.genresToString()
                runtimeTextView.text = String.format("%d мин", movie.runTime)
                voteTextView.text = String.format("%.1f (%d)", movie.vote, movie.voteCount)
                budgetTextView.text = String.format("Бюджет: %d $", movie.budget)
                revenueTextView.text = String.format("Доход: %d $", movie.revenue)
                releaseTextView.text = movie.releaseDate
                overviewTextView.text = movie.overview
            }
        }
    }

    private fun onLoadFailed(flag: String?) {
//        when (flag) {
//            DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
//            DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
//            DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
//            DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
//            DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
//        }

        binding.detailsFragmentRootView.showSnackBar(
            getString(R.string.error),
            getString(R.string.reload)
        ) { getMovie() }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        length: Int = Snackbar.LENGTH_INDEFINITE,
        action: ((View) -> Unit)? = null
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
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