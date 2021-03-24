package com.example.gb_movies.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gb_movies.R
import com.example.gb_movies.databinding.ActivityMainBinding
import com.example.gb_movies.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> navigate(HomeFragment())
                R.id.navigation_search -> navigate(SearchFragment())
                R.id.navigation_favorite -> true
                R.id.navigation_settings -> navigate(SettingsFragment())
            }
            return@setOnNavigationItemSelectedListener true
        }
        binding.navView.selectedItemId = R.id.navigation_home
    }

    private fun navigate(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragmentToRemove = getVisibleFragment(fragmentManager)
        if (fragmentToRemove != null) {
            fragmentTransaction.remove(fragmentToRemove)
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun getVisibleFragment(fragmentManager: FragmentManager): Fragment? {
        val fragments = fragmentManager.fragments
        val countFragments = fragments.size
        for (i in countFragments - 1 downTo 0) {
            val fragment = fragments[i]
            if (fragment.isVisible) return fragment
        }
        return null
    }

}