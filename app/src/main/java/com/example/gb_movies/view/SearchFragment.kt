package com.example.gb_movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gb_movies.R


class SearchFragment : Fragment() {

    companion object {
        val TAG: String = HomeFragment::class.java.name

        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


}