package com.example.examapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.examapp.R
import com.example.examapp.Service.ExamService
import com.example.examapp.Util.Util
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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.createExamFragment -> visibleBottomNav(false)
                R.id.examResultFragment -> visibleBottomNav(false)
                else -> visibleBottomNav(true)
            }
        }

        binding.bottomNavView.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { /* No Operation.. */ }
        }
        navigateToExamFragmentIfNeeded(intent)
    }

    private fun visibleBottomNav(visible: Boolean) {
        binding.apply {
            bottomNavView.isVisible = visible
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToExamFragmentIfNeeded(intent)
    }

    private fun navigateToExamFragmentIfNeeded(intent: Intent?) {
        intent?.action?.let {
            if (it == "ACTION_SHOW_EXAM_FRAGMENT" ||
                (ExamService.status.value != null && ExamService.status.value != Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING))
            {
                navController.navigate(R.id.action_global_examFragment)
            }
        }
    }

}