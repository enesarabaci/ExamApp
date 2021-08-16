package com.example.examapp.View

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.examapp.R
import com.example.examapp.ViewModel.ExamsViewModel
import com.example.examapp.databinding.FragmentExamsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExamsFragment : Fragment(R.layout.fragment_exams) {

    private lateinit var binding: FragmentExamsBinding
    private val viewModel: ExamsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentExamsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        collectData()
        binding.fragmentExamsFab.setOnClickListener {
            findNavController().navigate(R.id.action_examsFragment_to_createExamFragment)
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { list ->
                if (list.isEmpty()) {
                    binding.fragmentExamsEmptyLayout.visibility = View.VISIBLE
                }else {
                    binding.fragmentExamsEmptyLayout.visibility = View.GONE
                    //Rv operations..
                }
            }
        }
    }

}