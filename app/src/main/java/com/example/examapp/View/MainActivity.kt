package com.example.examapp.View

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.examapp.R
import com.example.examapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.nav_graph).addOnDestinationChangedListener { controller, destination, arguments ->
            // ** Hide nav operation ** //
        }

        binding.bottomNavView.apply {
            background = null
            menu.getItem(2).isEnabled = false
            setupWithNavController(findNavController(R.id.fragmentContainerView))
        }
    }

}