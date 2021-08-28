package com.example.examapp.View

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examapp.Adapter.ExamResultAdapter
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.R
import com.example.examapp.Util.Util.snackbarBuilder
import com.example.examapp.ViewModel.ExamResultViewModel
import com.example.examapp.databinding.FragmentExamResultBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class ExamResultFragment : Fragment(R.layout.fragment_exam_result) {

    private lateinit var binding: FragmentExamResultBinding
    private val recyclerAdapter = ExamResultAdapter()
    private val viewModel: ExamResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentExamResultBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentExamResultToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentExamResultToolbar.setTitle("Sonuçları Gir")

        val examWithLectures = arguments?.get("examWithLectures") as ExamWithLectures?
        examWithLectures?.let {
            viewModel.examWithLectures = it
            recyclerAdapter.updateList(it.lectures)
            recyclerAdapter.elimination = it.exam.elimination
        }
        setupRv()

        binding.fragmentExamResultButton.setOnClickListener {
            val result = viewModel.saveExamResult(requireView())
            if (result) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
                findNavController().navigate(ExamResultFragmentDirections.actionExamResultFragmentToResultsFragment(), navOptions)
                snackbarBuilder(requireView(), "kayıt tamamlandı")
            }
        }
    }

    private fun setupRv() {
        binding.fragmentExamResultRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setOnTruesTextListener { position, count ->
            viewModel.examWithLectures?.let {
                try {
                    val countInt = count.toInt()
                    viewModel.trueResultsMap.put(it.lectures.get(position).name, countInt)
                }catch (e: Exception) {
                    viewModel.trueResultsMap.remove(it.lectures.get(position).name)
                }
            }
        }
        recyclerAdapter.setOnFalsesTextListener { position, count ->
            viewModel.examWithLectures?.let {
                try {
                    val countInt = count.toInt()
                    viewModel.falseResultsMap.put(it.lectures.get(position).name, countInt)
                }catch (e: Exception) {
                    viewModel.falseResultsMap.remove(it.lectures.get(position).name)
                }
            }
        }
    }

}