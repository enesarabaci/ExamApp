package com.example.examapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.examapp.R
import com.example.examapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.createExamFragment -> visibleBottomNav(false)
                else -> visibleBottomNav(true)
            }
        }

        binding.bottomNavView.apply {
            background = null
            menu.getItem(2).isEnabled = false
            setupWithNavController(navController)
        }
    }

    private fun visibleBottomNav(visible: Boolean) {
        binding.apply {
            bottomAppBar.isVisible = visible
            bottomFab.isVisible = visible
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}