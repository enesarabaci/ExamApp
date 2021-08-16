package com.example.examapp.View

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.examapp.R
import com.example.examapp.databinding.FragmentCreateExamBinding

class CreateExamFragment : Fragment(R.layout.fragment_create_exam) {

    private lateinit var binding: FragmentCreateExamBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCreateExamBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentCreateExamToolbar)
            setupActionBarWithNavController(findNavController())
        }
        /*
        binding.fragmentCreateExamToolbar.setNavigationOnClickListener {
            activity?.onNavigateUp()
        }
        */
    }

}